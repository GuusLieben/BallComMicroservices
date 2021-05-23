package com.ball.auth.rest;

import com.ball.auth.amqp.RabbitMQSender;
import com.ball.auth.jwt.JwtTokenUtil;
import com.ball.auth.model.ErrorObject;
import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;
import com.ball.auth.model.jwt.UserJwtModel;
import com.ball.auth.model.rest.LoginModel;
import com.ball.auth.model.rest.PermitBody;
import com.ball.auth.model.rest.TokenValidationModel;
import com.ball.auth.model.rest.UserCreateModel;
import com.ball.auth.model.rest.UserPatchModel;
import com.ball.auth.model.rest.VerifiedModel;
import com.ball.auth.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final Hasher hasher;
    private final UserRepository userRepository;
    private final RabbitMQSender sender;
    private final JwtTokenUtil tokenUtil;
    private final ObjectMapper mapper;

    public AuthController(Hasher hasher, UserRepository userRepository, RabbitMQSender sender, JwtTokenUtil tokenUtil) {
        this.hasher = hasher;
        this.userRepository = userRepository;
        this.sender = sender;
        this.tokenUtil = tokenUtil;
        this.mapper = new ObjectMapper();
    }

    // If the payload is absent the user is not verified and can therefore be rejected immediately
    @PatchMapping("/user")
    public Object processShipment(@RequestBody UserPatchModel patchModel, @RequestHeader("X-Token-Payload") String payload) {
        Map<String, Object> payloadMap = new HashMap<>();
        if (payload != null) {
            payloadMap = this.extract(payload);
        }

        if (payloadMap.isEmpty()) return new ErrorObject(HttpStatus.UNAUTHORIZED, "You are not logged in");

        UserRole role = UserRole.valueOf(String.valueOf(payloadMap.get("role")).toUpperCase(Locale.ROOT));
        String email = (String) payloadMap.getOrDefault("email", null);
        // Customers and suppliers can only modify their own user data, employees are allowed to edit anyone
        if (!patchModel.getEmail().equals(email) && !UserRole.SYSTEM.equals(role))
            return new ErrorObject(HttpStatus.UNAUTHORIZED, "You are not allowed to patch this user");

        Optional<User> lookup = this.userRepository.findByEmailEquals(patchModel.getEmail());
        if (lookup.isPresent()) {
            User user = lookup.get();

            for (Entry<String, String> entry : patchModel.getMeta().entrySet()) {
                if (JwtTokenUtil.SKIP_KEYS.contains(entry.getKey())) {
                    return new ErrorObject(HttpStatus.UNAUTHORIZED, "Metadata key '" + entry.getKey() + "' is reserved and cannot be used");
                }
                if (entry.getValue() == null) user.getMeta().remove(entry.getKey());
                else user.getMeta().put(entry.getKey(), entry.getValue());
            }

            User saved = this.userRepository.save(user);
            saved.setPasswordHash(null);

            this.sender.userUpdated(saved);
            saved.setGuid(null);
            return saved;
        }
        else {
            return new ErrorObject(404, "Not found", "User with email " + patchModel.getEmail() + " does not exist");
        }
    }

    @PostMapping("/register")
    public Object createUser(@RequestBody UserCreateModel user) {
        Optional<User> lookup = this.userRepository.findByEmailEquals(user.getEmail());
        if (lookup.isPresent()) {
            return new ErrorObject(HttpStatus.BAD_REQUEST, "User with email " + user.getEmail() + " already exists");
        }
        else {
            User temp = new User();
            temp.setEmail(user.getEmail());
            temp.setPasswordHash(this.hasher.hashToString(12, user.getPassword().toCharArray()));
            temp.setRole(user.getRole());
            temp.setMeta(new HashMap<>());
            temp.getMeta().put("firstName", user.getFirstName());
            temp.getMeta().put("lastName", user.getLastName());
            User saved = this.userRepository.save(temp);
            this.sender.userCreated(saved);
            return saved;
        }
    }

    @PostMapping("/verify")
    public TokenValidationModel isValidSession(@RequestHeader("X-token") String token) {
        return new TokenValidationModel(token, !this.tokenUtil.isTokenExpired(token));
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginModel model) {
        Optional<User> user = this.userRepository.findByEmailEquals(model.getEmail());
        if (user.isPresent()) {
            Result result = BCrypt.verifyer().verify(
                    model.getPassword().toCharArray(),
                    user.get().getPasswordHash()
            );
            if (result.verified) {
                return new VerifiedModel(this.tokenUtil.generateToken(user.get()));
            }
            else {
                return new ErrorObject(HttpStatus.UNAUTHORIZED, "Invalid login credentials");
            }
        }
        else {
            return new ErrorObject(HttpStatus.UNAUTHORIZED, "No user with that email exists");
        }
    }

    @GetMapping("/permit")
    public PermitBody isPermittedRequest(@RequestHeader("X-token") String token, @RequestParam("target") long expected) {
        if (this.tokenUtil.isTokenExpired(token)) return PermitBody.deny();
        UserJwtModel userPayload = this.tokenUtil.getUserPayload(token);
        EnumSet<UserRole> permittedRoles = UserRole.getStatusFlags(expected);
        if (permittedRoles.contains(userPayload.getRole())) {
            Map<String, Object> payload = this.tokenUtil.getRawPayload(token);
            return new PermitBody(true, payload);
        } else {
            return PermitBody.deny();
        }
    }

    private Map<String, Object> extract(String payload) {
        try {
            return this.mapper.readValue(payload, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

}

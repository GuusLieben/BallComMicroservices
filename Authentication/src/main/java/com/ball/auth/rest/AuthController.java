package com.ball.auth.rest;

import com.ball.auth.amqp.RabbitMQSender;
import com.ball.auth.jwt.JwtTokenUtil;
import com.ball.auth.model.ErrorObject;
import com.ball.auth.model.User;
import com.ball.auth.model.rest.LoginModel;
import com.ball.auth.model.rest.TokenValidationModel;
import com.ball.auth.model.rest.UserCreateModel;
import com.ball.auth.model.rest.UserPatchModel;
import com.ball.auth.model.rest.VerifiedModel;
import com.ball.auth.repository.UserRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    public AuthController(Hasher hasher, UserRepository userRepository, RabbitMQSender sender, JwtTokenUtil tokenUtil) {
        this.hasher = hasher;
        this.userRepository = userRepository;
        this.sender = sender;
        this.tokenUtil = tokenUtil;
    }

    @RequestMapping(value = "/user", method = RequestMethod.PATCH)
    public Object processShipment(@RequestBody UserPatchModel patchModel) {
        Optional<User> lookup = this.userRepository.findByEmailEquals(patchModel.getEmail());
        if (lookup.isPresent()) {
            User user = lookup.get();

            for (Entry<String, String> entry : patchModel.getMeta().entrySet()) {
                if (JwtTokenUtil.SKIP_KEYS.contains(entry.getKey())) {
                    return new ErrorObject(403, "Illegal key", "Metadata key '" + entry.getKey() + "' is reserved and cannot be used");
                }
                user.getMeta().put(entry.getKey(), entry.getValue());
            }
            return this.userRepository.save(user);
        }
        else {
            return new ErrorObject(404, "Not found", "User with email " + patchModel.getEmail() + " does not exist");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object createUser(@RequestBody UserCreateModel user) {
        Optional<User> lookup = this.userRepository.findByEmailEquals(user.getEmail());
        if (lookup.isPresent()) {
            return new ErrorObject(400, "Already exists", "User with email " + user.getEmail() + " already exists");
        }
        else {
            User temp = new User();
            temp.setEmail(user.getEmail());
            temp.setPasswordHash(this.hasher.hashToString(12, user.getPassword().toCharArray()));
            temp.setRole(user.getRole());
            User saved = this.userRepository.save(temp);
            this.sender.userCreated(saved);
            return saved;
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public TokenValidationModel isValidSession(@RequestHeader("X-token") String token) {
        return new TokenValidationModel(token, !this.tokenUtil.isTokenExpired(token));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
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
                return new ErrorObject(403, "Not permitted", "Invalid login credentials");
            }
        }
        else {
            return new ErrorObject(403, "Not permitted", "No user with that email exists");
        }
    }

    @RequestMapping(value = "/payload", method = RequestMethod.POST)
    public Object getPayload(@RequestHeader("X-token") String token) {
        return this.tokenUtil.getRawPayload(token);
    }

}

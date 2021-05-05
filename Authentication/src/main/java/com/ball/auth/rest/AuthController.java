package com.ball.auth.rest;

import com.ball.auth.amqp.RabbitMQSender;
import com.ball.auth.model.ErrorObject;
import com.ball.auth.model.User;
import com.ball.auth.model.UserMeta;
import com.ball.auth.model.rest.UserPatchModel;
import com.ball.auth.repository.UserMetadataRepository;
import com.ball.auth.repository.UserRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class AuthController {

    private final UserRepository userRepository;
    private final UserMetadataRepository metadataRepository;
    private final RabbitMQSender sender;

    public AuthController(UserRepository userRepository, UserMetadataRepository metadataRepository, RabbitMQSender sender) {
        this.userRepository = userRepository;
        this.metadataRepository = metadataRepository;
        this.sender = sender;
    }

    @RequestMapping(value = "/users", method = RequestMethod.PATCH)
    public Object processShipment(@RequestBody UserPatchModel patchModel) {
        Optional<User> lookup = this.userRepository.findByEmailEquals(patchModel.getEmail());
        if (lookup.isPresent()) {
            User user = lookup.get();
            for (UserMeta newMetadata : patchModel.getMeta()) {
                newMetadata.setMetaId(null);
                newMetadata.setUser(user);
                user.getMeta().add(newMetadata);
            }
            this.metadataRepository.saveAll(patchModel.getMeta());
            return this.userRepository.save(user);
        }
        else {
            return new ErrorObject(404, "Not found", "User with email " + patchModel.getEmail() + " does not exist");
        }
    }

}

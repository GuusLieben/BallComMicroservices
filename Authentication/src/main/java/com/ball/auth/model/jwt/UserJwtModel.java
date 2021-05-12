package com.ball.auth.model.jwt;

import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserJwtModel {

    private UUID guid;
    private Map<String, String> meta;
    private String email;
    private UserRole role;

    public UserJwtModel(User user) {
        this.guid = user.getGuid();
        this.meta = user.getMeta();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserJwtModel)) return false;
        UserJwtModel model = (UserJwtModel) o;
        return this.getGuid().equals(model.getGuid()) && this.getEmail().equals(model.getEmail()) && this.getRole() == model.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getGuid(), this.getEmail(), this.getRole());
    }
}

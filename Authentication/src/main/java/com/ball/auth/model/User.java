package com.ball.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@JsonInclude(Include.NON_DEFAULT)
public class User implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue
    private Long userId;

    @OneToMany(mappedBy = "user")
    private List<UserMeta> meta;

    private String email;
    private String passwordHash;

    private UserRole role;

}

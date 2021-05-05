package com.ball.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrePersist;
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
    @Column(name = "id")
    private Long userId;

    private UUID guid;

    @ElementCollection
    @CollectionTable(name = "user_meta", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "`key`")
    @Column(name = "`value`")
    private Map<String, String> meta;

    private String email;
    private String passwordHash;

    private UserRole role;

    @PrePersist
    protected void onCreate() {
        this.setGuid(java.util.UUID.randomUUID());
    }
}

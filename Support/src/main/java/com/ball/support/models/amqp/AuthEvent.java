package com.ball.support.models.amqp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
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
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@Entity
public class AuthEvent implements Event {

    @Id
    @JsonIgnore
    @GeneratedValue
    @Column(name = "id")
    private Long eventId;

    private UUID guid;

    @ElementCollection
    @CollectionTable(name = "event_meta", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "`key`")
    @Column(name = "`value`")
    private Map<String, String> meta;

    private String email;
    private String role;

    @PrePersist
    protected void onCreate() {
        this.setGuid(java.util.UUID.randomUUID());
    }

    @PostLoad
    protected void afterLoad() {
        if (this.getMeta() == null) this.setMeta(new HashMap<>());
    }

}

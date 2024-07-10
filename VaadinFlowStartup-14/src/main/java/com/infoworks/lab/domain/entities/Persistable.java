package com.infoworks.lab.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.models.events.EventType;
import com.it.soul.lab.sql.entity.PrimaryKey;

import javax.persistence.*;

@MappedSuperclass
public class Persistable<ID,VERSION> extends Response {

    @PrimaryKey(name="id", auto=true) @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Version @JsonIgnore
    private VERSION version;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public VERSION getVersion() {
        return version;
    }

    public void setVersion(VERSION version) {
        this.version = version;
    }

    @JsonIgnore
    public String getPrimaryKeyName() {
        if (getClass().isAnnotationPresent(AttributeOverride.class)){
            AttributeOverride attr = getClass().getAnnotation(AttributeOverride.class);
            return attr.column().name();
        }
        return "id";
    }

    @JsonIgnore
    private String uuid;
    @JsonIgnore
    private String timestamp;
    @JsonIgnore
    private EventType eventType;

    @JsonIgnore @Transient
    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}

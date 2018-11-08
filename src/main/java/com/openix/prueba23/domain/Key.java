package com.openix.prueba23.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b> Key entity. </b>
 * 
 * @author GWP (Osvaldo)
 * @version 1.0
 * @since 17/11/2017
 */
@ApiModel(description = "<b> Key entity. </b>@author GWP (Osvaldo) @version 1.0 @since 17/11/2017")
@Entity
@Table(name = "key")
public class Key implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "used")
    private Boolean used;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToOne    @JoinColumn(unique = true)
    private Registry registry;

    @ManyToOne
    @JsonIgnoreProperties("")
    private App app;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Key code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isUsed() {
        return used;
    }

    public Key used(Boolean used) {
        this.used = used;
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Key deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Key registry(Registry registry) {
        this.registry = registry;
        return this;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public App getApp() {
        return app;
    }

    public Key app(App app) {
        this.app = app;
        return this;
    }

    public void setApp(App app) {
        this.app = app;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Key key = (Key) o;
        if (key.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), key.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Key{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", used='" + isUsed() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}

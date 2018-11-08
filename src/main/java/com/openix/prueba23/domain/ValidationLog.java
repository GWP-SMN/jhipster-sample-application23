package com.openix.prueba23.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * <b> ValidationLog entity. </b>
 * 
 * @author GWP (Osvaldo)
 * @version 1.0
 * @since 21/11/2017
 */
@ApiModel(description = "<b> ValidationLog entity. </b>@author GWP (Osvaldo) @version 1.0 @since 21/11/2017")
@Entity
@Table(name = "validation_log")
public class ValidationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_time")
    private Instant dateTime;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Registry registry;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public ValidationLog dateTime(Instant dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Float getLatitude() {
        return latitude;
    }

    public ValidationLog latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public ValidationLog longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Boolean isSuccess() {
        return success;
    }

    public ValidationLog success(Boolean success) {
        this.success = success;
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public ValidationLog deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Registry getRegistry() {
        return registry;
    }

    public ValidationLog registry(Registry registry) {
        this.registry = registry;
        return this;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
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
        ValidationLog validationLog = (ValidationLog) o;
        if (validationLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), validationLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ValidationLog{" +
            "id=" + getId() +
            ", dateTime='" + getDateTime() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", success='" + isSuccess() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}

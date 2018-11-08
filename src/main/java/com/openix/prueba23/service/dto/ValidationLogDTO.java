package com.openix.prueba23.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ValidationLog entity.
 */
public class ValidationLogDTO implements Serializable {

    private Long id;

    private Instant dateTime;

    private Float latitude;

    private Float longitude;

    private Boolean success;

    private Boolean deleted;

    private Long registryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Long registryId) {
        this.registryId = registryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidationLogDTO validationLogDTO = (ValidationLogDTO) o;
        if (validationLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), validationLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ValidationLogDTO{" +
            "id=" + getId() +
            ", dateTime='" + getDateTime() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", success='" + isSuccess() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", registry=" + getRegistryId() +
            "}";
    }
}

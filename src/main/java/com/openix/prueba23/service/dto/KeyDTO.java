package com.openix.prueba23.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Key entity.
 */
public class KeyDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Boolean used;

    private Boolean deleted;

    private Long registryId;

    private Long appId;

    private String appName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyDTO keyDTO = (KeyDTO) o;
        if (keyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), keyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KeyDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", used='" + isUsed() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", registry=" + getRegistryId() +
            ", app=" + getAppId() +
            ", app='" + getAppName() + "'" +
            "}";
    }
}

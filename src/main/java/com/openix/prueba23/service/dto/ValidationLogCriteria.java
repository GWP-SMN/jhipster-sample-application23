package com.openix.prueba23.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ValidationLog entity. This class is used in ValidationLogResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /validation-logs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ValidationLogCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateTime;

    private FloatFilter latitude;

    private FloatFilter longitude;

    private BooleanFilter success;

    private BooleanFilter deleted;

    private LongFilter registryId;

    public ValidationLogCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateTime() {
        return dateTime;
    }

    public void setDateTime(InstantFilter dateTime) {
        this.dateTime = dateTime;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
    }

    public FloatFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(FloatFilter longitude) {
        this.longitude = longitude;
    }

    public BooleanFilter getSuccess() {
        return success;
    }

    public void setSuccess(BooleanFilter success) {
        this.success = success;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public LongFilter getRegistryId() {
        return registryId;
    }

    public void setRegistryId(LongFilter registryId) {
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
        final ValidationLogCriteria that = (ValidationLogCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateTime, that.dateTime) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(success, that.success) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(registryId, that.registryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateTime,
        latitude,
        longitude,
        success,
        deleted,
        registryId
        );
    }

    @Override
    public String toString() {
        return "ValidationLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateTime != null ? "dateTime=" + dateTime + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (success != null ? "success=" + success + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
                (registryId != null ? "registryId=" + registryId + ", " : "") +
            "}";
    }

}

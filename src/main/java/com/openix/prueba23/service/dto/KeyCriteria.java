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

/**
 * Criteria class for the Key entity. This class is used in KeyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /keys?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KeyCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private BooleanFilter used;

    private BooleanFilter deleted;

    private LongFilter registryId;

    private LongFilter appId;

    public KeyCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public BooleanFilter getUsed() {
        return used;
    }

    public void setUsed(BooleanFilter used) {
        this.used = used;
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

    public LongFilter getAppId() {
        return appId;
    }

    public void setAppId(LongFilter appId) {
        this.appId = appId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KeyCriteria that = (KeyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(used, that.used) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(registryId, that.registryId) &&
            Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        used,
        deleted,
        registryId,
        appId
        );
    }

    @Override
    public String toString() {
        return "KeyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (used != null ? "used=" + used + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
                (registryId != null ? "registryId=" + registryId + ", " : "") +
                (appId != null ? "appId=" + appId + ", " : "") +
            "}";
    }

}

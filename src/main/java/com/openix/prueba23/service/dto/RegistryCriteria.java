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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Registry entity. This class is used in RegistryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /registries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RegistryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter dni;

    private LocalDateFilter birthdate;

    private StringFilter phoneNumber;

    private StringFilter business;

    private StringFilter occupation;

    private StringFilter email;

    private StringFilter activationKey;

    private StringFilter validationCode;

    private StringFilter resetKey;

    private InstantFilter resetDate;

    private BooleanFilter activated;

    private InstantFilter activationDate;

    private BooleanFilter deleted;

    private LongFilter appId;

    private LongFilter subcategoryId;

    public RegistryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getDni() {
        return dni;
    }

    public void setDni(StringFilter dni) {
        this.dni = dni;
    }

    public LocalDateFilter getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDateFilter birthdate) {
        this.birthdate = birthdate;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getBusiness() {
        return business;
    }

    public void setBusiness(StringFilter business) {
        this.business = business;
    }

    public StringFilter getOccupation() {
        return occupation;
    }

    public void setOccupation(StringFilter occupation) {
        this.occupation = occupation;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(StringFilter activationKey) {
        this.activationKey = activationKey;
    }

    public StringFilter getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(StringFilter validationCode) {
        this.validationCode = validationCode;
    }

    public StringFilter getResetKey() {
        return resetKey;
    }

    public void setResetKey(StringFilter resetKey) {
        this.resetKey = resetKey;
    }

    public InstantFilter getResetDate() {
        return resetDate;
    }

    public void setResetDate(InstantFilter resetDate) {
        this.resetDate = resetDate;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public InstantFilter getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(InstantFilter activationDate) {
        this.activationDate = activationDate;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public LongFilter getAppId() {
        return appId;
    }

    public void setAppId(LongFilter appId) {
        this.appId = appId;
    }

    public LongFilter getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(LongFilter subcategoryId) {
        this.subcategoryId = subcategoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegistryCriteria that = (RegistryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(business, that.business) &&
            Objects.equals(occupation, that.occupation) &&
            Objects.equals(email, that.email) &&
            Objects.equals(activationKey, that.activationKey) &&
            Objects.equals(validationCode, that.validationCode) &&
            Objects.equals(resetKey, that.resetKey) &&
            Objects.equals(resetDate, that.resetDate) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(activationDate, that.activationDate) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(subcategoryId, that.subcategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        dni,
        birthdate,
        phoneNumber,
        business,
        occupation,
        email,
        activationKey,
        validationCode,
        resetKey,
        resetDate,
        activated,
        activationDate,
        deleted,
        appId,
        subcategoryId
        );
    }

    @Override
    public String toString() {
        return "RegistryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (dni != null ? "dni=" + dni + ", " : "") +
                (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (business != null ? "business=" + business + ", " : "") +
                (occupation != null ? "occupation=" + occupation + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (activationKey != null ? "activationKey=" + activationKey + ", " : "") +
                (validationCode != null ? "validationCode=" + validationCode + ", " : "") +
                (resetKey != null ? "resetKey=" + resetKey + ", " : "") +
                (resetDate != null ? "resetDate=" + resetDate + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
                (activationDate != null ? "activationDate=" + activationDate + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
                (appId != null ? "appId=" + appId + ", " : "") +
                (subcategoryId != null ? "subcategoryId=" + subcategoryId + ", " : "") +
            "}";
    }

}

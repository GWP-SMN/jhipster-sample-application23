package com.openix.prueba23.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * <b> Registry entity. </b>
 * 
 * @author GWP (Osvaldo)
 * @version 1.0
 * @since 21/11/2017
 */
@ApiModel(description = "<b> Registry entity. </b>@author GWP (Osvaldo) @version 1.0 @since 21/11/2017")
@Entity
@Table(name = "registry")
public class Registry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dni")
    private String dni;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "business")
    private String business;

    @Column(name = "occupation")
    private String occupation;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "activation_key")
    private String activationKey;

    @Column(name = "validation_code")
    private String validationCode;

    @Column(name = "reset_key")
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "activation_date")
    private Instant activationDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties("")
    private App app;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Subcategory subcategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Registry firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Registry lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public Registry dni(String dni) {
        this.dni = dni;
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Registry birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Registry phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBusiness() {
        return business;
    }

    public Registry business(String business) {
        this.business = business;
        return this;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getOccupation() {
        return occupation;
    }

    public Registry occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public Registry email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public Registry activationKey(String activationKey) {
        this.activationKey = activationKey;
        return this;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public Registry validationCode(String validationCode) {
        this.validationCode = validationCode;
        return this;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getResetKey() {
        return resetKey;
    }

    public Registry resetKey(String resetKey) {
        this.resetKey = resetKey;
        return this;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public Registry resetDate(Instant resetDate) {
        this.resetDate = resetDate;
        return this;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Registry activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Instant getActivationDate() {
        return activationDate;
    }

    public Registry activationDate(Instant activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(Instant activationDate) {
        this.activationDate = activationDate;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Registry deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public App getApp() {
        return app;
    }

    public Registry app(App app) {
        this.app = app;
        return this;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public Registry subcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
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
        Registry registry = (Registry) o;
        if (registry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Registry{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dni='" + getDni() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", business='" + getBusiness() + "'" +
            ", occupation='" + getOccupation() + "'" +
            ", email='" + getEmail() + "'" +
            ", activationKey='" + getActivationKey() + "'" +
            ", validationCode='" + getValidationCode() + "'" +
            ", resetKey='" + getResetKey() + "'" +
            ", resetDate='" + getResetDate() + "'" +
            ", activated='" + isActivated() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}

package com.openix.prueba23.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b> App entity. </b>
 * 
 * @author GWP (Osvaldo)
 * @version 1.0
 * @since 17/11/2017
 */
@ApiModel(description = "<b> App entity. </b>@author GWP (Osvaldo) @version 1.0 @since 17/11/2017")
@Entity
@Table(name = "app")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "image_blob_content_type")
    private String imageBlobContentType;

    @Column(name = "image")
    private String image;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "deleted")
    private Boolean deleted;

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

    public String getName() {
        return name;
    }

    public App name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public App description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public App version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public App imageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return imageBlobContentType;
    }

    public App imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public String getImage() {
        return image;
    }

    public App image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean isActivated() {
        return activated;
    }

    public App activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public App deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public App subcategory(Subcategory subcategory) {
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
        App app = (App) o;
        if (app.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), app.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "App{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", version='" + getVersion() + "'" +
            ", imageBlob='" + getImageBlob() + "'" +
            ", imageBlobContentType='" + getImageBlobContentType() + "'" +
            ", image='" + getImage() + "'" +
            ", activated='" + isActivated() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}

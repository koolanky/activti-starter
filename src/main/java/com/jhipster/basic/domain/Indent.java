package com.jhipster.basic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.jhipster.basic.domain.enumeration.CurrentStatus;

/**
 * A Indent.
 */
@Entity
@Table(name = "indent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "indent")
public class Indent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "indent_id", nullable = false)
    private Long indentId;

    @NotNull
    @Column(name = "material_type", nullable = false)
    private String materialType;

    @NotNull
    @Column(name = "material_code", nullable = false)
    private String materialCode;

    @Column(name = "budgeted")
    private Boolean budgeted;

    @NotNull
    @Column(name = "indents", nullable = false)
    private String indents;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private CurrentStatus currentStatus;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndentId() {
        return indentId;
    }

    public Indent indentId(Long indentId) {
        this.indentId = indentId;
        return this;
    }

    public void setIndentId(Long indentId) {
        this.indentId = indentId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public Indent materialType(String materialType) {
        this.materialType = materialType;
        return this;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public Indent materialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Boolean isBudgeted() {
        return budgeted;
    }

    public Indent budgeted(Boolean budgeted) {
        this.budgeted = budgeted;
        return this;
    }

    public void setBudgeted(Boolean budgeted) {
        this.budgeted = budgeted;
    }

    public String getIndents() {
        return indents;
    }

    public Indent indents(String indents) {
        this.indents = indents;
        return this;
    }

    public void setIndents(String indents) {
        this.indents = indents;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public Indent currentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
        return this;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public User getUser() {
        return user;
    }

    public Indent user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Indent indent = (Indent) o;
        if (indent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), indent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Indent{" +
            "id=" + getId() +
            ", indentId='" + getIndentId() + "'" +
            ", materialType='" + getMaterialType() + "'" +
            ", materialCode='" + getMaterialCode() + "'" +
            ", budgeted='" + isBudgeted() + "'" +
            ", indents='" + getIndents() + "'" +
            ", currentStatus='" + getCurrentStatus() + "'" +
            "}";
    }
}

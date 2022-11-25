package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad Fabricante.
 */
@Entity
@Table(name = "fabricante")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fabricante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre
     */
    @NotNull
    @Size(max = 300)
    @Column(name = "nombre", length = 300, nullable = false)
    private String nombre;

    /**
     * domicilio
     */
    @NotNull
    @Size(max = 300)
    @Column(name = "domicilio", length = 300, nullable = false)
    private String domicilio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fabricante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Fabricante nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public Fabricante domicilio(String domicilio) {
        this.setDomicilio(domicilio);
        return this;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fabricante)) {
            return false;
        }
        return id != null && id.equals(((Fabricante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fabricante{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", domicilio='" + getDomicilio() + "'" +
            "}";
    }
}

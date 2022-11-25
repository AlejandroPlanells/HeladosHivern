package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad Ingrediente.
 */
@Entity
@Table(name = "ingrediente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ingrediente implements Serializable {

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
     * descripcion
     */
    @NotNull
    @Size(max = 300)
    @Column(name = "descripcion", length = 300, nullable = false)
    private String descripcion;

    /**
     * gramos
     */
    @NotNull
    @Column(name = "gr", nullable = false)
    private Float gr;

    /**
     * calorias
     */
    @NotNull
    @Size(max = 150)
    @Column(name = "cal", length = 150, nullable = false)
    private String cal;

    @ManyToMany(mappedBy = "ingredientes")
    @JsonIgnoreProperties(value = { "fabricante", "ingredientes" }, allowSetters = true)
    private Set<Helado> helados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ingrediente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Ingrediente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Ingrediente descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getGr() {
        return this.gr;
    }

    public Ingrediente gr(Float gr) {
        this.setGr(gr);
        return this;
    }

    public void setGr(Float gr) {
        this.gr = gr;
    }

    public String getCal() {
        return this.cal;
    }

    public Ingrediente cal(String cal) {
        this.setCal(cal);
        return this;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public Set<Helado> getHelados() {
        return this.helados;
    }

    public void setHelados(Set<Helado> helados) {
        if (this.helados != null) {
            this.helados.forEach(i -> i.removeIngredientes(this));
        }
        if (helados != null) {
            helados.forEach(i -> i.addIngredientes(this));
        }
        this.helados = helados;
    }

    public Ingrediente helados(Set<Helado> helados) {
        this.setHelados(helados);
        return this;
    }

    public Ingrediente addHelados(Helado helado) {
        this.helados.add(helado);
        helado.getIngredientes().add(this);
        return this;
    }

    public Ingrediente removeHelados(Helado helado) {
        this.helados.remove(helado);
        helado.getIngredientes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingrediente)) {
            return false;
        }
        return id != null && id.equals(((Ingrediente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingrediente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", gr=" + getGr() +
            ", cal='" + getCal() + "'" +
            "}";
    }
}

package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad Helado.
 */
@Entity
@Table(name = "helado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Helado implements Serializable {

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
     * oferta
     */
    @NotNull
    @Column(name = "en_oferta", nullable = false)
    private Boolean enOferta;

    /**
     * precio oferta
     */
    @Column(name = "precio_oferta")
    private Double precioOferta;

    /**
     * precio
     */
    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    /**
     * fechaCreacion
     */
    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @ManyToOne(optional = false)
    @NotNull
    private Fabricante fabricante;

    @ManyToMany
    @JoinTable(
        name = "rel_helado__ingredientes",
        joinColumns = @JoinColumn(name = "helado_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredientes_id")
    )
    @JsonIgnoreProperties(value = { "helados" }, allowSetters = true)
    private Set<Ingrediente> ingredientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Helado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Helado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEnOferta() {
        return this.enOferta;
    }

    public Helado enOferta(Boolean enOferta) {
        this.setEnOferta(enOferta);
        return this;
    }

    public void setEnOferta(Boolean enOferta) {
        this.enOferta = enOferta;
    }

    public Double getPrecioOferta() {
        return this.precioOferta;
    }

    public Helado precioOferta(Double precioOferta) {
        this.setPrecioOferta(precioOferta);
        return this;
    }

    public void setPrecioOferta(Double precioOferta) {
        this.precioOferta = precioOferta;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Helado precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Helado fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Fabricante getFabricante() {
        return this.fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public Helado fabricante(Fabricante fabricante) {
        this.setFabricante(fabricante);
        return this;
    }

    public Set<Ingrediente> getIngredientes() {
        return this.ingredientes;
    }

    public void setIngredientes(Set<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Helado ingredientes(Set<Ingrediente> ingredientes) {
        this.setIngredientes(ingredientes);
        return this;
    }

    public Helado addIngredientes(Ingrediente ingrediente) {
        this.ingredientes.add(ingrediente);
        ingrediente.getHelados().add(this);
        return this;
    }

    public Helado removeIngredientes(Ingrediente ingrediente) {
        this.ingredientes.remove(ingrediente);
        ingrediente.getHelados().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Helado)) {
            return false;
        }
        return id != null && id.equals(((Helado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Helado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", enOferta='" + getEnOferta() + "'" +
            ", precioOferta=" + getPrecioOferta() +
            ", precio=" + getPrecio() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            "}";
    }
}

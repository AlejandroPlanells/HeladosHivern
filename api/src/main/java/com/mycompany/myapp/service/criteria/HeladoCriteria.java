package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Helado} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.HeladoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /helados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HeladoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private BooleanFilter enOferta;

    private DoubleFilter precioOferta;

    private DoubleFilter precio;

    private InstantFilter fechaCreacion;

    private LongFilter fabricanteId;

    private LongFilter ingredientesId;

    private Boolean distinct;

    public HeladoCriteria() {}

    public HeladoCriteria(HeladoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.enOferta = other.enOferta == null ? null : other.enOferta.copy();
        this.precioOferta = other.precioOferta == null ? null : other.precioOferta.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.fechaCreacion = other.fechaCreacion == null ? null : other.fechaCreacion.copy();
        this.fabricanteId = other.fabricanteId == null ? null : other.fabricanteId.copy();
        this.ingredientesId = other.ingredientesId == null ? null : other.ingredientesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HeladoCriteria copy() {
        return new HeladoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public BooleanFilter getEnOferta() {
        return enOferta;
    }

    public BooleanFilter enOferta() {
        if (enOferta == null) {
            enOferta = new BooleanFilter();
        }
        return enOferta;
    }

    public void setEnOferta(BooleanFilter enOferta) {
        this.enOferta = enOferta;
    }

    public DoubleFilter getPrecioOferta() {
        return precioOferta;
    }

    public DoubleFilter precioOferta() {
        if (precioOferta == null) {
            precioOferta = new DoubleFilter();
        }
        return precioOferta;
    }

    public void setPrecioOferta(DoubleFilter precioOferta) {
        this.precioOferta = precioOferta;
    }

    public DoubleFilter getPrecio() {
        return precio;
    }

    public DoubleFilter precio() {
        if (precio == null) {
            precio = new DoubleFilter();
        }
        return precio;
    }

    public void setPrecio(DoubleFilter precio) {
        this.precio = precio;
    }

    public InstantFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public InstantFilter fechaCreacion() {
        if (fechaCreacion == null) {
            fechaCreacion = new InstantFilter();
        }
        return fechaCreacion;
    }

    public void setFechaCreacion(InstantFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LongFilter getFabricanteId() {
        return fabricanteId;
    }

    public LongFilter fabricanteId() {
        if (fabricanteId == null) {
            fabricanteId = new LongFilter();
        }
        return fabricanteId;
    }

    public void setFabricanteId(LongFilter fabricanteId) {
        this.fabricanteId = fabricanteId;
    }

    public LongFilter getIngredientesId() {
        return ingredientesId;
    }

    public LongFilter ingredientesId() {
        if (ingredientesId == null) {
            ingredientesId = new LongFilter();
        }
        return ingredientesId;
    }

    public void setIngredientesId(LongFilter ingredientesId) {
        this.ingredientesId = ingredientesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HeladoCriteria that = (HeladoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(enOferta, that.enOferta) &&
            Objects.equals(precioOferta, that.precioOferta) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(fabricanteId, that.fabricanteId) &&
            Objects.equals(ingredientesId, that.ingredientesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, enOferta, precioOferta, precio, fechaCreacion, fabricanteId, ingredientesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HeladoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (enOferta != null ? "enOferta=" + enOferta + ", " : "") +
            (precioOferta != null ? "precioOferta=" + precioOferta + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
            (fabricanteId != null ? "fabricanteId=" + fabricanteId + ", " : "") +
            (ingredientesId != null ? "ingredientesId=" + ingredientesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

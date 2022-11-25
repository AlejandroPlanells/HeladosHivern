package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Ingrediente} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.IngredienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ingredientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngredienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private FloatFilter gr;

    private StringFilter cal;

    private LongFilter heladosId;

    private Boolean distinct;

    public IngredienteCriteria() {}

    public IngredienteCriteria(IngredienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.gr = other.gr == null ? null : other.gr.copy();
        this.cal = other.cal == null ? null : other.cal.copy();
        this.heladosId = other.heladosId == null ? null : other.heladosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IngredienteCriteria copy() {
        return new IngredienteCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public FloatFilter getGr() {
        return gr;
    }

    public FloatFilter gr() {
        if (gr == null) {
            gr = new FloatFilter();
        }
        return gr;
    }

    public void setGr(FloatFilter gr) {
        this.gr = gr;
    }

    public StringFilter getCal() {
        return cal;
    }

    public StringFilter cal() {
        if (cal == null) {
            cal = new StringFilter();
        }
        return cal;
    }

    public void setCal(StringFilter cal) {
        this.cal = cal;
    }

    public LongFilter getHeladosId() {
        return heladosId;
    }

    public LongFilter heladosId() {
        if (heladosId == null) {
            heladosId = new LongFilter();
        }
        return heladosId;
    }

    public void setHeladosId(LongFilter heladosId) {
        this.heladosId = heladosId;
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
        final IngredienteCriteria that = (IngredienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(gr, that.gr) &&
            Objects.equals(cal, that.cal) &&
            Objects.equals(heladosId, that.heladosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, gr, cal, heladosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (gr != null ? "gr=" + gr + ", " : "") +
            (cal != null ? "cal=" + cal + ", " : "") +
            (heladosId != null ? "heladosId=" + heladosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

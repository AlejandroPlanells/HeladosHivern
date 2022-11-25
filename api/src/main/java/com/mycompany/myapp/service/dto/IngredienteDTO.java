package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Ingrediente} entity.
 */
@Schema(description = "Entidad Ingrediente.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngredienteDTO implements Serializable {

    private Long id;

    /**
     * nombre
     */
    @NotNull
    @Size(max = 300)
    @Schema(description = "nombre", required = true)
    private String nombre;

    /**
     * descripcion
     */
    @NotNull
    @Size(max = 300)
    @Schema(description = "descripcion", required = true)
    private String descripcion;

    /**
     * gramos
     */
    @NotNull
    @Schema(description = "gramos", required = true)
    private Float gr;

    /**
     * calorias
     */
    @NotNull
    @Size(max = 150)
    @Schema(description = "calorias", required = true)
    private String cal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getGr() {
        return gr;
    }

    public void setGr(Float gr) {
        this.gr = gr;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredienteDTO)) {
            return false;
        }

        IngredienteDTO ingredienteDTO = (IngredienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingredienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredienteDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", gr=" + getGr() +
            ", cal='" + getCal() + "'" +
            "}";
    }
}

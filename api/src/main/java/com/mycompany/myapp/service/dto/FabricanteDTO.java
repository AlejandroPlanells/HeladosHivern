package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Fabricante} entity.
 */
@Schema(description = "Entidad Fabricante.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FabricanteDTO implements Serializable {

    private Long id;

    /**
     * nombre
     */
    @NotNull
    @Size(max = 300)
    @Schema(description = "nombre", required = true)
    private String nombre;

    /**
     * domicilio
     */
    @NotNull
    @Size(max = 300)
    @Schema(description = "domicilio", required = true)
    private String domicilio;

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

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FabricanteDTO)) {
            return false;
        }

        FabricanteDTO fabricanteDTO = (FabricanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fabricanteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FabricanteDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", domicilio='" + getDomicilio() + "'" +
            "}";
    }
}

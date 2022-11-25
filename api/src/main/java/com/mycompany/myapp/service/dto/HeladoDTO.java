package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Helado} entity.
 */
@Schema(description = "Entidad Helado.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HeladoDTO implements Serializable {

    private Long id;

    /**
     * nombre
     */
    @NotNull
    @Size(max = 300)
    @Schema(description = "nombre", required = true)
    private String nombre;

    /**
     * oferta
     */
    @NotNull
    @Schema(description = "oferta", required = true)
    private Boolean enOferta;

    /**
     * precio oferta
     */
    @Schema(description = "precio oferta")
    private Double precioOferta;

    /**
     * precio
     */
    @NotNull
    @Schema(description = "precio", required = true)
    private Double precio;

    /**
     * fechaCreacion
     */
    @NotNull
    @Schema(description = "fechaCreacion", required = true)
    private Instant fechaCreacion;

    private FabricanteDTO fabricante;

    private Set<IngredienteDTO> ingredientes = new HashSet<>();

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

    public Boolean getEnOferta() {
        return enOferta;
    }

    public void setEnOferta(Boolean enOferta) {
        this.enOferta = enOferta;
    }

    public Double getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(Double precioOferta) {
        this.precioOferta = precioOferta;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public FabricanteDTO getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteDTO fabricante) {
        this.fabricante = fabricante;
    }

    public Set<IngredienteDTO> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(Set<IngredienteDTO> ingredientes) {
        this.ingredientes = ingredientes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HeladoDTO)) {
            return false;
        }

        HeladoDTO heladoDTO = (HeladoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, heladoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HeladoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", enOferta='" + getEnOferta() + "'" +
            ", precioOferta=" + getPrecioOferta() +
            ", precio=" + getPrecio() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fabricante=" + getFabricante() +
            ", ingredientes=" + getIngredientes() +
            "}";
    }
}

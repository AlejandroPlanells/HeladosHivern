package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Fabricante;
import com.mycompany.myapp.domain.Helado;
import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.service.dto.FabricanteDTO;
import com.mycompany.myapp.service.dto.HeladoDTO;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Helado} and its DTO {@link HeladoDTO}.
 */
@Mapper(componentModel = "spring")
public interface HeladoMapper extends EntityMapper<HeladoDTO, Helado> {
    @Mapping(target = "fabricante", source = "fabricante", qualifiedByName = "fabricanteId")
    @Mapping(target = "ingredientes", source = "ingredientes", qualifiedByName = "ingredienteIdSet")
    HeladoDTO toDto(Helado s);

    @Mapping(target = "removeIngredientes", ignore = true)
    Helado toEntity(HeladoDTO heladoDTO);

    @Named("fabricanteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FabricanteDTO toDtoFabricanteId(Fabricante fabricante);

    @Named("ingredienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IngredienteDTO toDtoIngredienteId(Ingrediente ingrediente);

    @Named("ingredienteIdSet")
    default Set<IngredienteDTO> toDtoIngredienteIdSet(Set<Ingrediente> ingrediente) {
        return ingrediente.stream().map(this::toDtoIngredienteId).collect(Collectors.toSet());
    }
}

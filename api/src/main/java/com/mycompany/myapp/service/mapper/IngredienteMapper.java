package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingrediente} and its DTO {@link IngredienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngredienteMapper extends EntityMapper<IngredienteDTO, Ingrediente> {}

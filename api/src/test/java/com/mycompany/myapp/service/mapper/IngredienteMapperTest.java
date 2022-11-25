package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredienteMapperTest {

    private IngredienteMapper ingredienteMapper;

    @BeforeEach
    public void setUp() {
        ingredienteMapper = new IngredienteMapperImpl();
    }
}

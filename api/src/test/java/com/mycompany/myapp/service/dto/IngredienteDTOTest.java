package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngredienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngredienteDTO.class);
        IngredienteDTO ingredienteDTO1 = new IngredienteDTO();
        ingredienteDTO1.setId(1L);
        IngredienteDTO ingredienteDTO2 = new IngredienteDTO();
        assertThat(ingredienteDTO1).isNotEqualTo(ingredienteDTO2);
        ingredienteDTO2.setId(ingredienteDTO1.getId());
        assertThat(ingredienteDTO1).isEqualTo(ingredienteDTO2);
        ingredienteDTO2.setId(2L);
        assertThat(ingredienteDTO1).isNotEqualTo(ingredienteDTO2);
        ingredienteDTO1.setId(null);
        assertThat(ingredienteDTO1).isNotEqualTo(ingredienteDTO2);
    }
}

package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HeladoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HeladoDTO.class);
        HeladoDTO heladoDTO1 = new HeladoDTO();
        heladoDTO1.setId(1L);
        HeladoDTO heladoDTO2 = new HeladoDTO();
        assertThat(heladoDTO1).isNotEqualTo(heladoDTO2);
        heladoDTO2.setId(heladoDTO1.getId());
        assertThat(heladoDTO1).isEqualTo(heladoDTO2);
        heladoDTO2.setId(2L);
        assertThat(heladoDTO1).isNotEqualTo(heladoDTO2);
        heladoDTO1.setId(null);
        assertThat(heladoDTO1).isNotEqualTo(heladoDTO2);
    }
}

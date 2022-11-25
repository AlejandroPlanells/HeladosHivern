package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HeladoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Helado.class);
        Helado helado1 = new Helado();
        helado1.setId(1L);
        Helado helado2 = new Helado();
        helado2.setId(helado1.getId());
        assertThat(helado1).isEqualTo(helado2);
        helado2.setId(2L);
        assertThat(helado1).isNotEqualTo(helado2);
        helado1.setId(null);
        assertThat(helado1).isNotEqualTo(helado2);
    }
}

package fr.polytech.info4.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalCooperativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NationalCooperative.class);
        NationalCooperative nationalCooperative1 = new NationalCooperative();
        nationalCooperative1.setId(1L);
        NationalCooperative nationalCooperative2 = new NationalCooperative();
        nationalCooperative2.setId(nationalCooperative1.getId());
        assertThat(nationalCooperative1).isEqualTo(nationalCooperative2);
        nationalCooperative2.setId(2L);
        assertThat(nationalCooperative1).isNotEqualTo(nationalCooperative2);
        nationalCooperative1.setId(null);
        assertThat(nationalCooperative1).isNotEqualTo(nationalCooperative2);
    }
}

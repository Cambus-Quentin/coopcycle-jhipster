package fr.polytech.info4.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalCooperativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalCooperative.class);
        LocalCooperative localCooperative1 = new LocalCooperative();
        localCooperative1.setId(1L);
        LocalCooperative localCooperative2 = new LocalCooperative();
        localCooperative2.setId(localCooperative1.getId());
        assertThat(localCooperative1).isEqualTo(localCooperative2);
        localCooperative2.setId(2L);
        assertThat(localCooperative1).isNotEqualTo(localCooperative2);
        localCooperative1.setId(null);
        assertThat(localCooperative1).isNotEqualTo(localCooperative2);
    }
}

package fr.polytech.info4.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DelivererDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DelivererDTO.class);
        DelivererDTO delivererDTO1 = new DelivererDTO();
        delivererDTO1.setId(1L);
        DelivererDTO delivererDTO2 = new DelivererDTO();
        assertThat(delivererDTO1).isNotEqualTo(delivererDTO2);
        delivererDTO2.setId(delivererDTO1.getId());
        assertThat(delivererDTO1).isEqualTo(delivererDTO2);
        delivererDTO2.setId(2L);
        assertThat(delivererDTO1).isNotEqualTo(delivererDTO2);
        delivererDTO1.setId(null);
        assertThat(delivererDTO1).isNotEqualTo(delivererDTO2);
    }
}

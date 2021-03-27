package fr.polytech.info4.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalCooperativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalCooperativeDTO.class);
        LocalCooperativeDTO localCooperativeDTO1 = new LocalCooperativeDTO();
        localCooperativeDTO1.setId(1L);
        LocalCooperativeDTO localCooperativeDTO2 = new LocalCooperativeDTO();
        assertThat(localCooperativeDTO1).isNotEqualTo(localCooperativeDTO2);
        localCooperativeDTO2.setId(localCooperativeDTO1.getId());
        assertThat(localCooperativeDTO1).isEqualTo(localCooperativeDTO2);
        localCooperativeDTO2.setId(2L);
        assertThat(localCooperativeDTO1).isNotEqualTo(localCooperativeDTO2);
        localCooperativeDTO1.setId(null);
        assertThat(localCooperativeDTO1).isNotEqualTo(localCooperativeDTO2);
    }
}

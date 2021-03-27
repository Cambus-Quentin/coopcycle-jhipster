package fr.polytech.info4.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalCooperativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NationalCooperativeDTO.class);
        NationalCooperativeDTO nationalCooperativeDTO1 = new NationalCooperativeDTO();
        nationalCooperativeDTO1.setId(1L);
        NationalCooperativeDTO nationalCooperativeDTO2 = new NationalCooperativeDTO();
        assertThat(nationalCooperativeDTO1).isNotEqualTo(nationalCooperativeDTO2);
        nationalCooperativeDTO2.setId(nationalCooperativeDTO1.getId());
        assertThat(nationalCooperativeDTO1).isEqualTo(nationalCooperativeDTO2);
        nationalCooperativeDTO2.setId(2L);
        assertThat(nationalCooperativeDTO1).isNotEqualTo(nationalCooperativeDTO2);
        nationalCooperativeDTO1.setId(null);
        assertThat(nationalCooperativeDTO1).isNotEqualTo(nationalCooperativeDTO2);
    }
}

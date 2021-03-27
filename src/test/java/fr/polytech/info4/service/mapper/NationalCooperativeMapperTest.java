package fr.polytech.info4.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NationalCooperativeMapperTest {

    private NationalCooperativeMapper nationalCooperativeMapper;

    @BeforeEach
    public void setUp() {
        nationalCooperativeMapper = new NationalCooperativeMapperImpl();
    }
}

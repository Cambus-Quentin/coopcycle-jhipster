package fr.polytech.info4.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalCooperativeMapperTest {

    private LocalCooperativeMapper localCooperativeMapper;

    @BeforeEach
    public void setUp() {
        localCooperativeMapper = new LocalCooperativeMapperImpl();
    }
}

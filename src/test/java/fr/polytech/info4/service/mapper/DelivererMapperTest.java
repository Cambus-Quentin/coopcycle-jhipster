package fr.polytech.info4.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DelivererMapperTest {

    private DelivererMapper delivererMapper;

    @BeforeEach
    public void setUp() {
        delivererMapper = new DelivererMapperImpl();
    }
}

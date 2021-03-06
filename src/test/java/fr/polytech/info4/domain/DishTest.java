package fr.polytech.info4.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.polytech.info4.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DishTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dish.class);
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(dish1.getId());
        assertThat(dish1).isEqualTo(dish2);
        dish2.setId(2L);
        assertThat(dish1).isNotEqualTo(dish2);
        dish1.setId(null);
        assertThat(dish1).isNotEqualTo(dish2);
    }
}

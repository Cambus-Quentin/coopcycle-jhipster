package fr.polytech.info4.repository;

import fr.polytech.info4.domain.Deliverer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deliverer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DelivererRepository extends JpaRepository<Deliverer, Long>, JpaSpecificationExecutor<Deliverer> {}

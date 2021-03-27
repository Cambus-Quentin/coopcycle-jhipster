package fr.polytech.info4.repository;

import fr.polytech.info4.domain.NationalCooperative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NationalCooperative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalCooperativeRepository
    extends JpaRepository<NationalCooperative, Long>, JpaSpecificationExecutor<NationalCooperative> {}

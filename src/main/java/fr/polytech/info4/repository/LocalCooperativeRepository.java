package fr.polytech.info4.repository;

import fr.polytech.info4.domain.LocalCooperative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocalCooperative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalCooperativeRepository extends JpaRepository<LocalCooperative, Long>, JpaSpecificationExecutor<LocalCooperative> {}

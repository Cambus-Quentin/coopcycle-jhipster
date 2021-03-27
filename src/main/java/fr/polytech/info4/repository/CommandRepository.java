package fr.polytech.info4.repository;

import fr.polytech.info4.domain.Command;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Command entity.
 */
@Repository
public interface CommandRepository extends JpaRepository<Command, Long>, JpaSpecificationExecutor<Command> {
    @Query(
        value = "select distinct command from Command command left join fetch command.dishes",
        countQuery = "select count(distinct command) from Command command"
    )
    Page<Command> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct command from Command command left join fetch command.dishes")
    List<Command> findAllWithEagerRelationships();

    @Query("select command from Command command left join fetch command.dishes where command.id =:id")
    Optional<Command> findOneWithEagerRelationships(@Param("id") Long id);
}

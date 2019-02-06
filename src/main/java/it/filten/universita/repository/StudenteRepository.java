package it.filten.universita.repository;

import it.filten.universita.domain.Studente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Studente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long>, JpaSpecificationExecutor<Studente> {

    @Query(value = "select distinct studente from Studente studente left join fetch studente.corsi",
        countQuery = "select count(distinct studente) from Studente studente")
    Page<Studente> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct studente from Studente studente left join fetch studente.corsi")
    List<Studente> findAllWithEagerRelationships();

    @Query("select studente from Studente studente left join fetch studente.corsi where studente.id =:id")
    Optional<Studente> findOneWithEagerRelationships(@Param("id") Long id);

}

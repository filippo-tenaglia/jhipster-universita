package it.filten.universita.repository;

import it.filten.universita.domain.Corso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Corso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long>, JpaSpecificationExecutor<Corso> {

}

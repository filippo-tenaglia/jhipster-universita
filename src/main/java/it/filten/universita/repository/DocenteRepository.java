package it.filten.universita.repository;

import it.filten.universita.domain.Docente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Docente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long>, JpaSpecificationExecutor<Docente> {

}

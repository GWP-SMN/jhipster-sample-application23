package com.openix.prueba23.repository;

import com.openix.prueba23.domain.ValidationLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ValidationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidationLogRepository extends JpaRepository<ValidationLog, Long>, JpaSpecificationExecutor<ValidationLog> {

}

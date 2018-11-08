package com.openix.prueba23.repository;

import com.openix.prueba23.domain.Registry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Registry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryRepository extends JpaRepository<Registry, Long>, JpaSpecificationExecutor<Registry> {

}

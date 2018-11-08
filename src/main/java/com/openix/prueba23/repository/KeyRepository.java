package com.openix.prueba23.repository;

import com.openix.prueba23.domain.Key;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Key entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyRepository extends JpaRepository<Key, Long>, JpaSpecificationExecutor<Key> {

}

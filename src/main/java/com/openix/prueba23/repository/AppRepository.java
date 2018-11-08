package com.openix.prueba23.repository;

import com.openix.prueba23.domain.App;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the App entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {

}

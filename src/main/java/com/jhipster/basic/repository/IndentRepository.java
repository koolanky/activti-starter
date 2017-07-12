package com.jhipster.basic.repository;

import com.jhipster.basic.domain.Indent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Indent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndentRepository extends JpaRepository<Indent,Long> {

    @Query("select indent from Indent indent where indent.user.login = ?#{principal.username}")
    List<Indent> findByUserIsCurrentUser();
    
}

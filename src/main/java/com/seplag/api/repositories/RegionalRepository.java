package com.seplag.api.repositories;

import com.seplag.api.domain.regionais.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalRepository extends JpaRepository<Regional,Long> {
    List<Regional> findByAtivoTrue();

    Optional<Regional> findByIdExternoAndAtivoTrue(Integer idExterno);
}

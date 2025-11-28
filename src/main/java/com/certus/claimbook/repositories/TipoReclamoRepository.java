package com.certus.claimbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certus.claimbook.entities.TipoReclamo;

public interface TipoReclamoRepository extends JpaRepository<TipoReclamo, Long> {}
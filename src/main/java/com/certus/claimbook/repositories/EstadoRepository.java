package com.certus.claimbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certus.claimbook.entities.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {}
package com.certus.claimbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certus.claimbook.entities.Respuesta;

import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    List<Respuesta> findByReclamoId(Long reclamoId);
}

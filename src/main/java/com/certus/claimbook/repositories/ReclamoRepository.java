package com.certus.claimbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certus.claimbook.entities.Reclamo;
import java.util.Optional;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
	Optional<Reclamo> findByCodigoSeguimiento(String codigo);

    long countByCodigoSeguimientoStartingWith(String prefix);
}

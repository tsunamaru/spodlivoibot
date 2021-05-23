package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.Ddos;

import java.util.Optional;

public interface DdosRepository extends JpaRepository<Ddos, Long> {

    Optional<Ddos> findByTelegramUserId(Long id);

    Ddos getById(Long id);

}

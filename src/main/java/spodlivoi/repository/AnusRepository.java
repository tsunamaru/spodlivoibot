package spodlivoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spodlivoi.entity.Anus;

@Repository
public interface AnusRepository extends JpaRepository<Anus, Integer> {

}


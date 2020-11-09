package spodlivoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spodlivoi.entity.Dicks;

@Repository
public interface DickRepository extends JpaRepository<Dicks, Integer> {

}


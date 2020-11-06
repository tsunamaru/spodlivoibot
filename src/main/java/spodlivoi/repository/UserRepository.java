package spodlivoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spodlivoi.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users getByChatIdAndUserId(long chatId, long userId);

}


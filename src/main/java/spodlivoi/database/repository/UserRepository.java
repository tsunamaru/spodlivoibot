package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.Chats;
import spodlivoi.database.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> getByChatIdAndUserName(long chatId, String username);

    Users getByChatIdAndUserId(long chatId, long userId);

    List<Users> getAllByChat(Chats chat);

    List<Users> getByUserId(long userId);
}


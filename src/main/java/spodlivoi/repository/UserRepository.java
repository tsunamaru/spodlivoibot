package spodlivoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spodlivoi.entity.Chats;
import spodlivoi.entity.Users;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users getByChatIdAndUserId(long chatId, long userId);
    List<Users> getAllByChat(Chats chat);

}


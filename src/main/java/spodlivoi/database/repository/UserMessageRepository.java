package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.Chats;
import spodlivoi.database.entity.UserMessage;

import java.util.List;
import java.util.UUID;

public interface UserMessageRepository extends JpaRepository<UserMessage, UUID> {

    List<UserMessage> findAllByChat(Chats chat);


}

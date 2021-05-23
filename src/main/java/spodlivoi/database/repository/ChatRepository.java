package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.Chats;

public interface ChatRepository extends JpaRepository<Chats, Integer> {

    Chats getByChatId(long chatId);
    Long getIdByChatId(long chatId);

}


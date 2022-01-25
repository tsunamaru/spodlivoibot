package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.Chats;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chats, Integer> {

    Chats getByChatId(long chatId);

    Long getIdByChatId(long chatId);

    List<Chats> findByChatIdIn(Collection<Long> chatId);

}


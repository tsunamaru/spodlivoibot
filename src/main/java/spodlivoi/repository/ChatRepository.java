package spodlivoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spodlivoi.entity.Chats;

@Repository
public interface ChatRepository extends JpaRepository<Chats, Integer> {

    Chats getByChatId(long chatId);

}


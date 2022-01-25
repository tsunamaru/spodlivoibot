package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spodlivoi.database.entity.ChatSetting;

import java.util.List;
import java.util.Optional;

public interface ChatSettingRepository extends JpaRepository<ChatSetting, Long> {
    Optional<ChatSetting> findByChatId(long chatId);

    @Query("select cs.id from ChatSetting cs where cs.shitPost = true")
    List<Long> getChatIdsByShitPostTrue();
}


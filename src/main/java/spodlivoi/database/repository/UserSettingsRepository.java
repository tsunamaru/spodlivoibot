package spodlivoi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spodlivoi.database.entity.UserSettings;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    UserSettings getById(Long id);

}

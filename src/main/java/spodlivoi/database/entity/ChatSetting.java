package spodlivoi.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ddos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChatSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_settings_id_generator")
    @SequenceGenerator(name = "user_settings_id_generator", sequenceName = "user_settings_id_generator_seq",
            allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long chatId;
    private Long timeForDeleteMessage;
    @Column(columnDefinition = "boolean not null default true")
    private boolean shitPost;
}

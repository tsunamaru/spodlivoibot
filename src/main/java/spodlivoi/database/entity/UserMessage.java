package spodlivoi.database.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_message")
@Data
public class UserMessage {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "chat", referencedColumnName = "id")
    private Chats chat;
    @ManyToOne
    @JoinColumn(name = "\"user\"", referencedColumnName = "id")
    private Users user;
    private String messageId;

}

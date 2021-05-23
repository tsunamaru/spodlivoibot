package spodlivoi.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "chats")
@Data
public class Chats implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "chat_id_generator")
    @SequenceGenerator(name = "chat_id_generator", sequenceName = "chat_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "chat_name", nullable = true, length = 50)
    private String chatName;
    @Basic
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @OneToMany(mappedBy = "chat")
    private List<Users> users;
    @OneToMany(mappedBy = "chat")
    private List<UserMessage> userMessages;

}

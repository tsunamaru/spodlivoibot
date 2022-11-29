package spodlivoi.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Chats implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_generator")
    @SequenceGenerator(name = "chat_id_generator", sequenceName = "chat_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "chat_name", nullable = true, length = 120)
    private String chatName;
    @Basic
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @OneToMany(mappedBy = "chat")
    @ToString.Exclude
    private List<Users> users;
    @OneToMany(mappedBy = "chat")
    @ToString.Exclude
    private List<UserMessage> userMessages;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Chats chats = (Chats) o;
        return Objects.equals(id, chats.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "chats")
public class Chats implements Serializable {
    private int id;
    private String chatName;
    private long chatId;
    private Collection<Users> users;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "chat_name", nullable = true, length = 50)
    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @Basic
    @Column(name = "chat_id", nullable = false)
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chats chats = (Chats) o;
        return id == chats.id &&
                chatId == chats.chatId &&
                Objects.equals(chatName, chats.chatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatName, chatId);
    }

    @OneToMany(mappedBy = "chat")
    public Collection<Users> getUsers() {
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
    }
}

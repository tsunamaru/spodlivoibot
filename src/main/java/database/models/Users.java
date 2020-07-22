package database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public class Users implements Serializable {
    private int id;
    private String userName;
    private int userId;
    private Chats chat;
    private Integer chatId;
    private Collection<Dicks> dicks;

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
    @Column(name = "user_name", nullable = true, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id &&
                userId == users.userId &&
                Objects.equals(userName, users.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userId);
    }

    @ManyToOne
    @JoinColumn(name = "chat", referencedColumnName = "id")
    public Chats getChat() {
        return chat;
    }
    public void setChat(Chats chat) {
        this.chat = chat;
    }

    @Basic
    @Column(name = "chat", nullable = true, updatable = false, insertable = false)
    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chat) {
        this.chatId = chat;
    }

    @OneToMany(mappedBy = "users")
    public Collection<Dicks> getDicks() {
        return dicks;
    }

    public void setDicks(Collection<Dicks> dicks) {
        this.dicks = dicks;
    }

}

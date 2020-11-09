package spodlivoi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "polzovatel") // ¯\_(ツ)_/¯
@Data
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_generator_seq")
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "user_name", nullable = true, length = 50)
    private String userName;
    @Basic
    @Column(name = "user_id", nullable = false)
    private long userId;
    @ManyToOne
    @JoinColumn(name = "chat", referencedColumnName = "id")
    private Chats chat;
    @OneToOne(mappedBy = "user")
    private Dicks dick;
    @OneToOne(mappedBy = "user")
    private Anus anus;
}

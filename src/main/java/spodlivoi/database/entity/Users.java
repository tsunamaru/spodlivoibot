package spodlivoi.database.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "polzovatel") // ¯\_(ツ)_/¯
@Data
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;
    @Basic
    @Column(name = "user_id", nullable = false)
    private long userId;
    private String firstName;
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "chat", referencedColumnName = "id")
    private Chats chat;
    @OneToOne(mappedBy = "user")
    private Dicks dick;
    @OneToOne(mappedBy = "user")
    private Anus anus;
    @OneToOne(mappedBy = "user")
    private Vagina vagina;
    @OneToOne(mappedBy = "user")
    private UserSettings settings;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

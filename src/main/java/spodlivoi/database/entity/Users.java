package spodlivoi.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "polzovatel") // ¯\_(ツ)_/¯
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "user_name", nullable = false, length = 120)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Users users = (Users) o;
        return Objects.equals(id, users.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

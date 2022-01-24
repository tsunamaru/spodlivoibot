package spodlivoi.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import spodlivoi.database.enums.Copypaste;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ddos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Ddos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ddos_id_generator")
    @SequenceGenerator(name = "ddos_id_generator", sequenceName = "ddos_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long telegramUserId;
    private Boolean active;
    private Copypaste copypaste;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Ddos ddos = (Ddos) o;
        return id != null && Objects.equals(id, ddos.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package spodlivoi.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class RollerModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anus_id_generator")
    @SequenceGenerator(name = "anus_id_generator", sequenceName = "anus_id_generator_seq", allocationSize = 1)
    private long id;
    @Column(name = "size", nullable = false, precision = 2)
    private int size;
    @Column(name = "last_measurement", nullable = false)
    private LocalDateTime lastMeasurement;
    @OneToOne
    @JoinColumn(name = "polzovatel", referencedColumnName = "id", nullable = false)
    private Users user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        RollerModel that = (RollerModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

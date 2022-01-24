package spodlivoi.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "anus")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class Anus extends RollerModel {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Anus anus = (Anus) o;
        return Objects.equals(getId(), anus.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

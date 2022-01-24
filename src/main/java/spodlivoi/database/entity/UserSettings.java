package spodlivoi.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import spodlivoi.database.enums.Gender;
import spodlivoi.database.enums.converter.GenderConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_settings_id_generator")
    @SequenceGenerator(name = "user_settings_id_generator", sequenceName = "user_settings_id_generator_seq",
            allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "\"user\"", referencedColumnName = "id", nullable = false)
    private Users user;
    @Convert(converter = GenderConverter.class)
    private Gender gender;
    private boolean rollAnus;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean rollVagina;
    private boolean rollDick;
    private boolean changeSetting;
    @Column(columnDefinition = "boolean default true", nullable = false)
    private boolean accessGenderChange;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserSettings that = (UserSettings) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

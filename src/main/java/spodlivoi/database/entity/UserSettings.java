package spodlivoi.database.entity;

import lombok.Data;
import spodlivoi.database.enums.Gender;
import spodlivoi.database.enums.converter.GenderConverter;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
@Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "user_settings_id_generator")
    @SequenceGenerator(name = "user_settings_id_generator", sequenceName = "user_settings_id_generator_seq", allocationSize = 1)
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

}

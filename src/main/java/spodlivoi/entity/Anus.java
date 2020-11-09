package spodlivoi.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anus")
@Data
public class Anus {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "anus_id_generator")
    @SequenceGenerator(name = "anus_id_generator", sequenceName = "anus_id_generator_seq")
    private long id;
    @Column(name = "size", nullable = false, precision = 2)
    private int size;
    @Column(name = "last_measurement", nullable = false)
    private LocalDateTime lastMeasurement;
    @OneToOne
    @JoinColumn(name = "polzovatel", referencedColumnName = "id", nullable = false)
    private Users user;

}

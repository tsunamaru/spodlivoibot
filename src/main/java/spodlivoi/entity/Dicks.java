package spodlivoi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "dicks")
@Data
public class Dicks implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "dick_id_generator")
    @SequenceGenerator(name = "dick_id_generator", sequenceName = "dick_id_generator_seq")
    private long id;

    @Column(name = "size", nullable = false, precision = 2)
    private int size;
    @Column(name = "last_measurement", nullable = false)
    private LocalDateTime lastMeasurement;
    @OneToOne
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    private Users user;

}

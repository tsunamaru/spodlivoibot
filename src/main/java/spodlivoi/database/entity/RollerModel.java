package spodlivoi.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class RollerModel implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "anus_id_generator")
    @SequenceGenerator(name = "anus_id_generator", sequenceName = "anus_id_generator_seq", allocationSize = 1)
    private long id;
    @Column(name = "size", nullable = false, precision = 2)
    private int size;
    @Column(name = "last_measurement", nullable = false)
    private LocalDateTime lastMeasurement;
    @OneToOne
    @JoinColumn(name = "polzovatel", referencedColumnName = "id", nullable = false)
    private Users user;

}

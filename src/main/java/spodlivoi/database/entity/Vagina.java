package spodlivoi.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vagina")
@Data
public class Vagina {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "vagina_id_generator")
    @SequenceGenerator(name = "vagina_id_generator", sequenceName = "vagina_id_generator_seq", allocationSize = 1)
    private long id;
    @Column(name = "size", nullable = false, precision = 2)
    private int size;
    @Column(name = "last_measurement", nullable = false)
    private LocalDateTime lastMeasurement;
    @OneToOne
    @JoinColumn(name = "polzovatel", referencedColumnName = "id", nullable = false)
    private Users user;
    
}

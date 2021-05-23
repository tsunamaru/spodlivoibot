package spodlivoi.database.entity;

import lombok.Data;
import spodlivoi.database.enums.Copypaste;

import javax.persistence.*;

@Entity
@Table(name = "ddos")
@Data
public class Ddos {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "ddos_id_generator")
    @SequenceGenerator(name = "ddos_id_generator", sequenceName = "ddos_id_generator_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long telegramUserId;
    private Boolean active;
    private Copypaste copypaste;

}

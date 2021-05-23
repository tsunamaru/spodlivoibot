package spodlivoi.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dicks")
@Data
@Builder
@NoArgsConstructor
public class Dicks extends RollerModel {
}

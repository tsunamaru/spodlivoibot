package spodlivoi.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vagina")
@Data
@Builder
@NoArgsConstructor
public class Vagina extends RollerModel {
}

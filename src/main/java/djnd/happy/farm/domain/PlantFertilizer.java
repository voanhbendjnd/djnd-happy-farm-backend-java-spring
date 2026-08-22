package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "plant_fertilizer")
@Entity
public class PlantFertilizer extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String frequency; // ex: 2g/week
    String dosage; //ex: 5g/chau 30cm

    String stage; // GROWTH -  BLOOMING - MAINTENANCE
    @Column(name = "is_primary")
    Boolean isPrimary; // true: showing short for user view moment (use for plant every year)
    @Column(name = "plant_id")
    Long plantId;
    @Column(name = "fertilizer_id")
    Long fertilizerId;

}

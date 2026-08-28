package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "plant_toxicity_part", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_plant_part_toxicity",
                columnNames = {"plant_part_id", "plant_toxicity_id"}
        )
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantToxicityPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_part_id", nullable = false)
    Long plantPartId;
    @NotNull
    @Column(name = "plant_toxicity_id",  nullable = false)
    Long plantToxicityId;
    @Column(name = "characteristic", columnDefinition = "MEDIUMTEXT")
    String characteristic;
}

package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "plant_care_fertilizers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_plant_care_fertilizer_stage",
                        columnNames = {
                                "plant_care_id",
                                "fertilizer_id",
                                "growth_stage_id"
                        }
                )
        }
)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantCareFertilizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
            @Column(name = "plant_care_id", nullable = false)
    Long plantCareId;
    @NotNull
            @Column(name = "fertilizer_id", nullable = false)
    Long fertilizerId;
    @NotNull
            @Column(name = "growth_stage_id", nullable = false)
    Long growthStageId;
    String dosage;
    String frequency;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

}

package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "plant_growth_stage", uniqueConstraints = {@UniqueConstraint(name = "ux_plant_growth_stage_hierarchy",columnNames = {"plant_id", "growth_stage_id"})})
public class PlantGrowthStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "growth_stage_id",  nullable = false)
    Long growthStageId;
    @NotNull
    @Column(name = "plant_id", nullable = false)
    Long plantId;

    String duration;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

}

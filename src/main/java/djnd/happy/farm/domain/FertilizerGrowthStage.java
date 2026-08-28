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

@Table(name = "fertilizer_growth_stage", uniqueConstraints = {@UniqueConstraint(name = "ux_fertilizer_growth_stage_hierarchy",
        columnNames = {"fertilizer_id", "growth_stage_id"})})
public class FertilizerGrowthStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "growth_stage_id", nullable = false)
    Long growthStageId;
    @NotNull
    @Column(name = "fertilizer_id", nullable = false)
    Long fertilizerId;
    String recommendedDosage;
    String frequency;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

}

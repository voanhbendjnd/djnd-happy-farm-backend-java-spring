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
@Table(name = "plant_distribution", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_plant_distribution",
                columnNames = {"plant_id", "distribution_id"}
        )
})
@Entity
public class PlantDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @NotNull
    @Column(name = "distribution_id", nullable = false)
    Long distributionId;
    String distributionType;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

}

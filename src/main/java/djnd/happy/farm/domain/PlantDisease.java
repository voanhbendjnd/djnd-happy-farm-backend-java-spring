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
@Table(name = "plant_disease", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_plant_disease",
                columnNames = {"plant_id", "disease_id"}

        )
})
public class PlantDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @NotNull
    @Column(name = "disease_id", nullable = false)
    Long diseaseId;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;


}

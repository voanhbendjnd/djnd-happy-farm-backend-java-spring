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
@Table(name = "plant_characteristic_part", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_plant_characteristic_plant_part",
                columnNames = {"plant_characteristic_part_id", "plant_part_id"}
        )
})
@Entity
public class PlantCharacteristicPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_characteristic_id", nullable = false)
    Long plantCharacteristicId;
    @NotNull
    @Column(name ="plant_part_id", nullable = false)
    Long plantPartId;
    @Column(name = "characteristic", columnDefinition = "MEDIUMTEXT")
    String characteristic;
}

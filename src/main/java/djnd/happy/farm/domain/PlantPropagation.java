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
@Table(name = "plant_propagation", uniqueConstraints = {
        @UniqueConstraint(name = "ux_plant_propagation_method", columnNames = {"plant_id", "propagation_id"})
})
public class PlantPropagation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @NotNull
    @Column(name = "propagation_id", nullable = false)
    Long propagationId;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

}

package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "plant_common_names", uniqueConstraints = @UniqueConstraint(columnNames = {"plant_id", "name", "language", "region"}, name = "ux_plant_common_name"))
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantCommonName extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @NotNull
    @Column(name = "name", nullable = false)
    String name;
    String language;
    String region;
    Boolean isPreferred;

}

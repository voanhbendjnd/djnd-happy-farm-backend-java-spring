package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "plant_characteristics")
@Entity
public class PlantCharacteristic extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", nullable = false, unique = true)
    Long plantId;

    @Column(name = "growth_habit")
    String growthHabit;
    @Column(name = "growth_rate")
    String growthRate;
    @Column(name = "mature_height_min")
    BigDecimal matureHeightMin;
    @Column(name = "mature_height_max")
    BigDecimal matureHeightMax;
    @Column(name ="mature_width_min")
    BigDecimal matureWidthMin;
    @Column(name = "mature_width_max")
    BigDecimal matureWidthMax;
    @Column(name = "size_unit")
    String sizeUnit;
}

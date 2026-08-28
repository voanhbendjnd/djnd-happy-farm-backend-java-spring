package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "plant_cares")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantCare extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "plant_id", unique = true, nullable = false)
    Long plantId;
    String watering;
    String light;
    BigDecimal minHumidity;
    BigDecimal maxHumidity;
    BigDecimal minTemperature;
    BigDecimal maxTemperature;
    String soil;
    String repotting;
    String pruning;
    String fertilizing;
    @Column(name = "special_care", columnDefinition = "MEDIUMTEXT")
    String specialCare;





}

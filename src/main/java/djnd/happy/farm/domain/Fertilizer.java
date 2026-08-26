package djnd.happy.farm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "fertilizers")
public class Fertilizer extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "name", unique = true, nullable = false)
    String name;
    String type;
    @Column(name = "npk_ratio")
    String npkRatio;
    @Column(name = "description", length = 500)
    String description;
    BigDecimal nitrogen;
    BigDecimal phosphorus;
    BigDecimal potassium;
    @JsonIgnore
    @ManyToMany
            @JoinTable(
                    name = "fertilizer_growth_stage",
                    joinColumns = @JoinColumn(name = "fertilizer_id"),
                    inverseJoinColumns = @JoinColumn(name = "growth_stage_id")
            )
    Set<GrowthStage>  growthStages = new HashSet<>();
}

package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "growth_stages")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowthStage extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "code", unique = true,  nullable = false)
    String code;
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    @ManyToMany(mappedBy = "growthStages")
    Set<Fertilizer> fertilizers = new HashSet<>();

}

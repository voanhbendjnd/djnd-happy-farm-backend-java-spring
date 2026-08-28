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
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "plant_toxicities")
public class PlantToxicity extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "plant_id", unique = true, nullable = false)
    Long plantId;
    // enum toxicity level
    @Column(name = "toxicity_level")
    Double toxicityLevel;
    @Column(name = "first_aid")
    String firstAid;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;


    @ManyToMany
            @JoinTable(
                    name ="plant_toxicity_symptom",
                    joinColumns = {@JoinColumn(name ="plant_toxicity_id")},
                    inverseJoinColumns = {@JoinColumn(name = "toxicity_symptom_id")}
            )
    Set<ToxicitySymptom> toxicitySymptoms = new HashSet<>();
}

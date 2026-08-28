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

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "pests")
public class Pest extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;



    @ManyToMany
            @JoinTable(
                    name = "pest_symptom_intermediary",
                    joinColumns = {@JoinColumn(name = "pest_id")},
                    inverseJoinColumns = {@JoinColumn(name = "pest_symptom_id")}
            )
    Set<PestSymptom> pestSymptoms = new HashSet<>();


}

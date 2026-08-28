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
@Table(name = "disease_treatment", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_disease_treatment",
                columnNames = {"disease_id", "treatment_id"}
        )
})
@Entity
public class DiseaseTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
            @Column(name = "disease_id", nullable = false)
    Long diseaseId;
    @NotNull
    @Column(name = "treatment_id", nullable = false)
    Long treatmentId;
    @Column(name = "dosage", columnDefinition = "MEDIUMTEXT")
    String dosage;
    String frequency;
    String duration;
    String precaution;

}

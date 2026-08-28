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
@Table(name = "pest_disease", uniqueConstraints = {
        @UniqueConstraint(
                name = "ux_pest_disease",
                columnNames = {"pest_id", "disease_id"}
        )
})
@Entity
public class PestDisease {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "pest_id", nullable = false)
    Long pestId;
    @NotNull
    @Column(name = "disease_id", nullable = false)
    Long diseaseId;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;
    String transmissionRole;
}

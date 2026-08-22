package djnd.happy.farm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "plant_reviews")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantReview extends AbstractAuditingEntity<Long> implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @Min(1)
    @Max(5)
    Integer rating;
    @Column(name = "user_id", nullable = false)
    Long userId;
    @Column(columnDefinition = "MEDIUMTEXT", name = "content")
    String content;
}

package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "plant_likes", uniqueConstraints = {@UniqueConstraint(name = "ux_user_plant_like", columnNames = {"user_id", "plant_id"})})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantLike extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "plant_id", nullable = false)
    Long plantId;
    @Column(name = "user_id", nullable = false)
    Long userId;
}

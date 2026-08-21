package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "plant_image")
public class PlantImage extends AbstractAuditingEntity <Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "plant_id")
    Long plantId;
    @Column(length = 500, name = "image_url")
    String imageUrl;
    @Column(name = "is_primary")// main image
    boolean isPrimary;
}

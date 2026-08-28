package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "growth_stages")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowthStage extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "code", unique = true,  nullable = false)
    String code;
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

}

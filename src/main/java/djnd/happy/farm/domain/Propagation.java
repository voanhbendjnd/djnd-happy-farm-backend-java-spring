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
/*
* N plant - N
* */
@Entity
@Getter
@Setter
@Table(name = "propagations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Propagation extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //enum
    @Column(name = "method")
    String method;
    @Column(name ="description", columnDefinition = "MEDIUMTEXT")
    String description;
    String difficulty;
}

package djnd.happy.farm.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "diseases")
public class Disease extends AbstractAuditingEntity<Long > implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "name", nullable = false,  unique = true)
    String name;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;
    // enum
    String severity;



}

package djnd.happy.farm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "habitats")
public class Habitat  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "name", nullable = false,  unique = true)
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

}

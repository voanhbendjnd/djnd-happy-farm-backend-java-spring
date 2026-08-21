package djnd.happy.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Table(name = "taxonomies", uniqueConstraints = {@UniqueConstraint(name = "ux_taxonomy_hierarchy",columnNames = {"kingdom", "family", "genus", "species"})})
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Taxonomy extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String kingdom;
    @Column(nullable = false)
    String family;
    String genus;
    String species;

}

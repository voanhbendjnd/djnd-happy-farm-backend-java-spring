package djnd.happy.farm.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "plants")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plant extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "taxonomy_id", nullable = false)
    Long taxonomyId;
    @NotNull
    @Column(name = "display_name", unique = true)
    String displayName;
    @Column(name = "scientific_name")
    String scientificName;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description_json", columnDefinition = "json")
    JsonNode descriptionJson;
    Boolean isCommunity;
    String status;
    @JoinTable(name = "plant_habitat",
            joinColumns = {@JoinColumn(name = "plant_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "habitat_name", referencedColumnName = "name")}
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    Set<Habitat> habitats = new HashSet<>();



}

package djnd.happy.farm.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "fertilizers")
public class Fertilizer extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "name", unique = true, nullable = false)
    String name;
    String type;
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;
    BigDecimal nitrogen;
    BigDecimal phosphorus;
    BigDecimal potassium;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description_json", columnDefinition = "json")
    JsonNode descriptionJson;

}

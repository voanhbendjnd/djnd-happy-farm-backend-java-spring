package djnd.happy.farm.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GbifSpeciesResponseDTO extends TaxonomyDTO {
    private Long usageKey;
    private String scientificName;
    private String canonicalName;
    private String rank;
    private String status;
}

package djnd.happy.farm.service;

import djnd.happy.farm.domain.Taxonomy;
import djnd.happy.farm.repository.TaxonomyRepository;
import djnd.happy.farm.service.dto.GbifSpeciesResponseDTO;
import djnd.happy.farm.service.errors.GlobalBadRequestException;
import djnd.happy.farm.service.errors.SpeciesNotFoudException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class TaxonomyService {
    final TaxonomyRepository taxonomyRepository;
    final RestClient restClient;
    private static final String EXCLUDED_KINGDOM = "Animalia";
    public TaxonomyService(RestClient.Builder clientBuilder, TaxonomyRepository taxonomyRepository) {
        this.taxonomyRepository = taxonomyRepository;
        this.restClient = clientBuilder
                .baseUrl("https://api.gbif.org/v1")
                .build();
    }
    public void createTaxonomy(String name) {
        GbifSpeciesResponseDTO gbif =this.matchAndGet(name);

        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setFamily(gbif.getFamily());
        taxonomy.setKingdom(gbif.getKingdom());
        taxonomy.setGenus(gbif.getGenus());
        taxonomy.setSpecies(gbif.getSpecies());
        taxonomyRepository.save(taxonomy);
    }

    public GbifSpeciesResponseDTO matchAndGet(String name){
        GbifSpeciesResponseDTO gbif = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/species/match")
                        .queryParam("name", name.trim())
                        .build()

                ).retrieve()
                .body(GbifSpeciesResponseDTO.class);
        if(gbif == null || gbif.getKingdom() == null || gbif.getFamily() == null || gbif.getSpecies() == null || gbif.getGenus() == null){
            throw new SpeciesNotFoudException();
        }
        if(!"SPECIES".equalsIgnoreCase(gbif.getRank())){
            throw new GlobalBadRequestException("Taxon must be a species", "taxonomyManagement", "taxonnotspecies");
        }
        if(gbif.getKingdom().equalsIgnoreCase(EXCLUDED_KINGDOM)){
            throw new GlobalBadRequestException("Animal taxonomy is not allowed", "taxonomyManagement", "taxonnotallowed");
        }
        return gbif;
    }

}

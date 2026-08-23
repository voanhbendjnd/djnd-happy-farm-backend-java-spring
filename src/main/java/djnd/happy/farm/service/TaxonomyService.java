package djnd.happy.farm.service;

import djnd.happy.farm.domain.Taxonomy;
import djnd.happy.farm.repository.TaxonomyRepository;
import djnd.happy.farm.service.dto.GbifSpeciesResponseDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.dto.TaxonomyDTO;
import djnd.happy.farm.service.errors.GlobalBadRequestException;
import djnd.happy.farm.service.errors.SpeciesNotFoudException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchAllWithFilterAndQuery(String q,String filterFamily, String filterGenus, Pageable pageable) {
        Page<Taxonomy> page = taxonomyRepository.fetchAll(q.trim(),filterFamily.trim(), filterGenus.trim(), pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(this::toDTO).toList());
        return res;
    }

    private Taxonomy toEntity(TaxonomyDTO dto) {
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setFamily(dto.getFamily());
        taxonomy.setKingdom(dto.getKingdom());
        taxonomy.setGenus(dto.getGenus());
        taxonomy.setSpecies(dto.getSpecies());
        return taxonomy;
    }
    private TaxonomyDTO toDTO(Taxonomy taxonomy) {
        TaxonomyDTO dto = new TaxonomyDTO();
        dto.setId(taxonomy.getId());
        dto.setKingdom(taxonomy.getKingdom());
        dto.setFamily(taxonomy.getFamily());
        dto.setGenus(taxonomy.getGenus());
        dto.setSpecies(taxonomy.getSpecies());
        return dto;
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

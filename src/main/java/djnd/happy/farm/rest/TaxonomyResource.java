package djnd.happy.farm.rest;

import com.turkraft.springfilter.boot.Filter;
import djnd.happy.farm.repository.TaxonomyRepository;
import djnd.happy.farm.service.TaxonomyService;
import djnd.happy.farm.service.dto.GbifSpeciesResponseDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.dto.TaxonomyDTO;
import djnd.happy.farm.util.annotation.ApiMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/api/taxonomies")
public class TaxonomyResource {

    final TaxonomyService taxonomyService;
    final TaxonomyRepository taxonomyRepository;
    private boolean taxonomyFound(String  kingdom, String family, String genus, String species) {
        return taxonomyRepository.existByKingdomAndFamilyAndGenusAndSpecies(kingdom, family, genus, species);
    }

    @GetMapping("/match")
    @ApiMessage("Check species name taxonomy and show data")
    public ResponseEntity<GbifSpeciesResponseDTO> match(@RequestParam(name = "name", required = true) String name){
        return ResponseEntity.ok(taxonomyService.matchAndGet(name));
    }

    @GetMapping
    @ApiMessage("Fetch taxonomies with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "family",required = false) String family,
            @RequestParam(name = "genus", required = false) String genus,
            Pageable pageable){
        return ResponseEntity.ok(taxonomyService.fetchAllWithFilterAndQuery(q != null ? q : "",family != null ? family :"", genus != null ? genus: "", pageable));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTaxonomyWithSpecies(@RequestParam(name = "name", required = true) String name){

        taxonomyService.createTaxonomy(name);
    }
}

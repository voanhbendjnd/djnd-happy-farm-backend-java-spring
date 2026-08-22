package djnd.happy.farm.rest;

import djnd.happy.farm.repository.TaxonomyRepository;
import djnd.happy.farm.rest.errors.ErrorConstants;
import djnd.happy.farm.service.TaxonomyService;
import djnd.happy.farm.service.dto.GbifSpeciesResponseDTO;
import djnd.happy.farm.service.dto.TaxonomyDTO;
import djnd.happy.farm.service.errors.SpeciesNotFoudException;
import djnd.happy.farm.util.annotation.ApiMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTaxonomyWithSpecies(@RequestParam(name = "name", required = true) String name){

        taxonomyService.createTaxonomy(name);
    }
}

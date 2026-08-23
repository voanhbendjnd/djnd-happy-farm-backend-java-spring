package djnd.happy.farm.web.rest;

import djnd.happy.farm.domain.Habitat;
import djnd.happy.farm.security.AuthoritiesConstants;
import djnd.happy.farm.service.HabitatService;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/habitats")
@RequiredArgsConstructor
public class HabitatResource {
    final HabitatService habitatService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void createHabitat(@Valid @RequestBody Habitat habitat) {
        habitatService.createHabitat(habitat);
    }


    @GetMapping
    @ApiMessage("Fetch all habitat with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll(Pageable pageable, @RequestParam(name = "q", required = false) String q) {
        return ResponseEntity.ok(habitatService.fetchAll(q != null ? q : "", pageable));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateHabitat(@Valid @RequestBody Habitat habitat) {
        habitatService.updateHabitat(habitat);
    }
}

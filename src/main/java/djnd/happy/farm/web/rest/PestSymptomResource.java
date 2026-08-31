package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.PestSymptomService;
import djnd.happy.farm.service.dto.PestSymptomDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pestSymptoms")
public class PestSymptomResource {
    final PestSymptomService pestSymptomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPestSymptom(@Valid @RequestBody PestSymptomDTO dto) {
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new pest symptom cannot already have an ID", "pestSymptomManagement", "idinbody");
        }
        pestSymptomService.createPestSymptom(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePestSymptom(@Valid @RequestBody PestSymptomDTO dto) {
        if(dto.getId() == null){
            throw new BadRequestExceptionGlobal("Cannot find ID in body pest symptom", "pestSymptomManagement", "idnotfound");
        }
        pestSymptomService.updatePestSymptom(dto);
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> fetchAllWithQuery(@RequestParam(name = "q", required = false) String q, Pageable pageable){
        return ResponseEntity.ok(pestSymptomService.fetchAllWithPagination(q, pageable));
    }
}

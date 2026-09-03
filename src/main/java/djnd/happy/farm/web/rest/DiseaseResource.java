package djnd.happy.farm.web.rest;

import djnd.happy.farm.domain.enums.DiseaseSeverity;
import djnd.happy.farm.service.DiseaseService;
import djnd.happy.farm.service.dto.DiseaseDTO;
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

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/api/diseases")
public class DiseaseResource {
    final DiseaseService diseaseService;
    private void checkSeverityValid(String severity){
        try{
            DiseaseSeverity.valueOf(severity);
        }catch(Exception e){
            throw new BadRequestExceptionGlobal("Severity Disease with name "+ severity + "invalid","diseaseManagement", "invalidformat");
        }
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody DiseaseDTO dto){
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new disease cannot already have an ID", "diseaseManagement", "includeid");
        }
        dto.setSeverity(dto.getSeverity().trim());
        checkSeverityValid(dto.getSeverity());
        diseaseService.createDisease(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody DiseaseDTO dto){
        if(dto.getId()== null){
            throw new BadRequestExceptionGlobal("Disease ID not found", "diseaseManegement", "notfoundid");
        }
        dto.setSeverity(dto.getSeverity().trim());
        checkSeverityValid(dto.getSeverity());
        diseaseService.updateDisease(dto);
    }

    public ResponseEntity<ResultPaginationDTO> fetchAll(@RequestParam(name = "q", required = false) String q, @RequestParam(name = "severity", required = false)String severity, Pageable pageable){
        String normalizedSeverity = "";
        if(severity != null && !severity.isEmpty()){
            normalizedSeverity = severity.trim();
            checkSeverityValid(normalizedSeverity);
        }
        return ResponseEntity.ok(diseaseService.fetchAll(q, normalizedSeverity, pageable));

    }
}

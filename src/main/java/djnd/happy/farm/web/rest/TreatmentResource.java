package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.TreatmentService;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.dto.TreatmentDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/treatments")
public class TreatmentResource {

    final TreatmentService treatmentService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTreatment(@Valid @RequestBody TreatmentDTO dto) {
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new treatment cannot already have an ID", "treatmentManagement", "bodyincludeid");
        }
        treatmentService.createTreatment(dto);
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateTreatment(@Valid @RequestBody TreatmentDTO dto) {
        if(dto.getId() == null){
            throw new BadRequestExceptionGlobal("Not found treatment ID when use feature update treatment", "treatmentManagement", "notfoundid");
        }
        treatmentService.updateTreatment(dto);
    }

    @GetMapping
    @ApiMessage("Fetch all treatment with condition")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@RequestParam(name = "q", required = false) String q, Pageable pageable) {
        return ResponseEntity.ok(treatmentService.fetchAll(q, pageable));
    }
}

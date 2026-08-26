package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.FertilizerService;
import djnd.happy.farm.service.dto.FertilizerGrowthStageDTO;
import djnd.happy.farm.service.dto.FertilizerSearchCriteriaDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fertilizers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FertilizerResource {
    final FertilizerService fertilizerService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFertilizer(@Valid @RequestBody FertilizerGrowthStageDTO dto) {
        if(dto.getId() != null) {
            throw new BadRequestExceptionGlobal("A new fertilizer cannot already have an ID", "fertilizerManagement", "bodyincludeid");
        }
        fertilizerService.createFertilizer(dto);
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateFertilizer(@Valid @RequestBody FertilizerGrowthStageDTO dto) {
        if(dto.getId() == null) {
            throw new BadRequestExceptionGlobal("Cannot not found ID for fertilizer", "fertilizerManagement", "notfoundid");
        }
        fertilizerService.updateFertilizer(dto);
    }

    @GetMapping
    @ApiMessage("Fetch fertilizer with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@ModelAttribute FertilizerSearchCriteriaDTO dto, Pageable pageable) {
        return ResponseEntity.ok(fertilizerService.fetchAll(dto, pageable));
    }
}

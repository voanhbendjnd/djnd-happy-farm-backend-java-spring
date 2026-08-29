package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.PlantPartService;
import djnd.happy.farm.service.dto.PlantPartDTO;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/api/plantParts")
public class PlantPartResource {
    final PlantPartService plantPartService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlantPart(@Valid @RequestBody PlantPartDTO plantPartDTO) {
        if(plantPartDTO.getId() != null) {
            throw new BadRequestExceptionGlobal("A new plant part cannot already have an ID", "plantPartManagement", "bodycontainid");
        }
        plantPartService.createPlantPart(plantPartDTO);
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePlantPart(@Valid @RequestBody PlantPartDTO plantPartDTO) {
        if(plantPartDTO.getId() == null) {
            throw new BadRequestExceptionGlobal("ID plant part not found", "plantPartManagement", "idnotfound");
        }
        plantPartService.updatePlantPart(plantPartDTO);
    }


    @GetMapping
    @ApiMessage("Fetch all plant part exist")
    public ResponseEntity<ResultPaginationDTO> getAllPlantParts(@RequestParam(name = "q", required = false) String q, Pageable pageable) {
        return ResponseEntity.ok(plantPartService.fetchAll(q, pageable));
    }
}

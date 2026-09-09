package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.PestDiseaseService;
import djnd.happy.farm.service.dto.PestDiseaseDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pestDiseases")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PestDiseaseResource {
    final PestDiseaseService pestDiseaseService;
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody PestDiseaseDTO dto){
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new relation cannot already have an ID", "pestDiseaseManagement", "bodyincludeid");
        }
        pestDiseaseService.createPestDisease(dto);
    }

    @PutMapping
    public void update(@Valid @RequestBody PestDiseaseDTO dto){
        if(dto.getId() == null){
            throw new BadRequestExceptionGlobal("Relation ID not found", "pestDiseaseManagement", "notfoundid");
        }
        pestDiseaseService.updatePestDisease(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id", required = true) @Positive Long id){
        pestDiseaseService.deletePestDisease(id);
    }

    @GetMapping("/byPest/{pestId}")
    public ResponseEntity<List<PestDiseaseDTO>> fetchByPest(@Positive @PathVariable(name = "pestId", required = true) Long pestId){
        return ResponseEntity.ok(pestDiseaseService.fetchByPestId(pestId));
    }
}

package djnd.happy.farm.web.rest;

import djnd.happy.farm.service.PestService;
import djnd.happy.farm.service.dto.PestDTO;
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
@RequestMapping("/api/pests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PestResource {
    final PestService pestService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPest(@Valid @RequestBody PestDTO dto){
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new pest cannot already have an ID", "pestManagement", "bodyincludeid");
        }
        pestService.createPest(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePest(@Valid @RequestBody PestDTO dto){
        if(dto.getId() == null){
            throw new BadRequestExceptionGlobal("Pest ID not found", "pestManagement", "notfoundid");
        }
        pestService.updatePest(dto);
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> fetchAll(@RequestParam(name = "q", required = false) String q, Pageable pageable){
        return ResponseEntity.ok(pestService.fetchAllWithQuery(q, pageable));
    }
}

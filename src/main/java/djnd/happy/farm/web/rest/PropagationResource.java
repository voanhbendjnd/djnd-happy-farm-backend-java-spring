package djnd.happy.farm.web.rest;

import djnd.happy.farm.domain.Propagation;
import djnd.happy.farm.domain.enums.PropagationDifficulty;
import djnd.happy.farm.domain.enums.PropagationMethod;
import djnd.happy.farm.service.PropagationService;
import djnd.happy.farm.service.dto.PropagationDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/propagations")
public class PropagationResource {
    final PropagationService propagationService;


    private void isValidDifficulty( String difficulty){
        try{
            PropagationDifficulty.valueOf(difficulty);
        }
        catch (Exception e){
            throw new BadRequestExceptionGlobal(String.format("Propagation difficulty %s invalid format", difficulty), "propagationManagement", "difficultyinvalidformat");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody PropagationDTO dto){
        if (dto.getDifficulty() != null) {
            isValidDifficulty( dto.getDifficulty());
        }
        if(dto.getId() != null){
            throw new BadRequestExceptionGlobal("A new propagation cannot already have an ID","propagationManagement", "idincludebody");
        }
        propagationService.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody PropagationDTO dto){
        if(dto.getDifficulty() != null){
            isValidDifficulty(dto.getDifficulty());
        }
        if(dto.getId() == null){
            throw new BadRequestExceptionGlobal("Cannot find ID propagation","propagationManagement", "idnotfound");
        }
        propagationService.update(dto);
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> fetchAll(@RequestParam(name = "method", required = false) String method, @RequestParam(name = "difficulty", required = false) String difficulty, Pageable pageable){
        return ResponseEntity.ok(propagationService.fetchAllWithMethod(method, difficulty, pageable));
    }
}

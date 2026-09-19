package djnd.happy.farm.web.rest;

import djnd.happy.farm.domain.enums.PlantStatus;
import djnd.happy.farm.service.FileService;
import djnd.happy.farm.service.PlantService;
import djnd.happy.farm.service.dto.PlantDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/plants")
public class PlantResource {
    final PlantService plantService;
    final FileService fileService;
    private void isValidStatus(String status){
        try{
            PlantStatus.valueOf(status);
        }
        catch(Exception e){
            throw new BadRequestExceptionGlobal("Status " + status + " invalid format", "plantManagement", "statusinvalidformat");
        }
    }
    @PostMapping("/save/images-to-temp")
    @ApiMessage("Save and get image url files to temp")
    public ResponseEntity<List<String>> savePlantImageToTemp(@RequestPart("files") List<MultipartFile> files) throws URISyntaxException, IOException {
            FileService.isValidFileImages(files);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveAndGetFilesURL(files));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewPlantByAdmin(@Valid @RequestBody PlantDTO plantDTO) throws URISyntaxException, IOException {
        if(plantDTO.getId() != null){
            throw new BadRequestExceptionGlobal("A new plant already cannot have an ID", "plantManagement", "bodyincludeid");
        }
        isValidStatus(plantDTO.getStatus());
        plantService.createNewPlantByAdmin(plantDTO);
    }
}

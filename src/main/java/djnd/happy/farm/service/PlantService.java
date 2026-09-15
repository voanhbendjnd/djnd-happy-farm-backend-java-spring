package djnd.happy.farm.service;

import djnd.happy.farm.domain.Plant;
import djnd.happy.farm.repository.PlantRepository;
import djnd.happy.farm.service.dto.PlantDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class PlantService {
    final PlantRepository  plantRepository;
    public void createNewPlantByAdmin(PlantDTO plantDTO) {
        String normalizedName = plantDTO.getDisplayName().trim();
        if(plantRepository.existsByNameIgnoreCase(normalizedName.toLowerCase())) {
            throw new DataConflictException(String.format("Plant with display name (%s) already exists", normalizedName), "plantManagement", "dataconflict");
        }
        Plant plant = new Plant();
        plant.setDisplayName(normalizedName);
        plant.setDescription(plantDTO.getDescription());
        plant.setStatus(plantDTO.getStatus());
        plant.setDescriptionJson(plantDTO.getDescriptionJson());
        plantRepository.save(plant);
    }

    public void updatePlantByAdmin(PlantDTO plantDTO) {
        String normalizedName = plantDTO.getDisplayName().trim();
        if(plantRepository.existsByNameIgnoreCaseAndIdNot(normalizedName.toLowerCase(), plantDTO.getId())) {
            throw new DataConflictException(String.format("Plant with display name (%s) already exists", normalizedName), "plantManagement", "dataconflict");
        }
        Plant currentPlant = plantRepository.findById(plantDTO.getId()).orElseThrow(() -> new DataResourceNotFoundException(String.format("Plant with ID %d not found", plantDTO.getId()), "plantManagement", "idnotfound"));
        currentPlant.setDisplayName(normalizedName);
        currentPlant.setDescription(plantDTO.getDescription());
        currentPlant.setStatus(plantDTO.getStatus());
        currentPlant.setDescriptionJson(plantDTO.getDescriptionJson());
        plantRepository.save(currentPlant);
    }


}

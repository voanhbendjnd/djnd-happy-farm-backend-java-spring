package djnd.happy.farm.service;

import djnd.happy.farm.domain.Plant;
import djnd.happy.farm.domain.PlantImage;
import djnd.happy.farm.repository.PlantImageRepository;
import djnd.happy.farm.repository.PlantRepository;
import djnd.happy.farm.service.dto.PlantDTO;
import djnd.happy.farm.service.dto.PlantImageDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class PlantService {
    final PlantRepository  plantRepository;
    final FileService fileService;
    final PlantImageRepository plantImageRepository;
    public void createNewPlantByAdmin(PlantDTO plantDTO) throws URISyntaxException, IOException {
        String normalizedName = plantDTO.getDisplayName().trim();
        if (plantRepository.existsByNameIgnoreCase(normalizedName.toLowerCase())) {
            throw new DataConflictException(String.format("Plant with display name (%s) already exists", normalizedName), "plantManagement", "dataconflict");
        }
        Plant plant = new Plant();
        plant.setDisplayName(normalizedName);
        plant.setDescription(plantDTO.getDescription());
        plant.setStatus(plantDTO.getStatus());
        plant.setDescriptionJson(plantDTO.getDescriptionJson());
        plantRepository.save(plant);
        if (plantDTO.getImages() != null && !plantDTO.getImages().isEmpty()) {
            List<PlantImage> newPlantImages = new ArrayList<>();
            long countIsPrimary = plantDTO.getImages().stream().filter(image -> Boolean.TRUE.equals(image.getIsPrimary())).count();
            if (countIsPrimary > 1) {
                throw new BadRequestExceptionGlobal(
                        "Main image for plant only 1 picture",
                        "plantManagement",
                        "imageprimarygreater1"
                );
            }
            if (countIsPrimary == 0) {
                throw new BadRequestExceptionGlobal(
                        "Plant must have one main image",
                        "plantManagement",
                        "imageprimarynotfound"
                );
            }
            for (PlantImageDTO imageDTO : plantDTO.getImages()) {

                String fileSaved = fileService.moveFileToSave(
                        imageDTO.getFileName(),
                        FileService.SAVE_IMAGE_PLANT
                );

                if (fileSaved == null) {
                    throw new BadRequestExceptionGlobal(
                            "Cannot move image file: " + imageDTO.getFileName(),
                            "plantManagement",
                            "imagemovenotfound"
                    );
                }

                PlantImage plantImage = new PlantImage();

                plantImage.setPlantId(plant.getId());
                plantImage.setImageUrl(fileSaved);
                plantImage.setIsPrimary(
                        Boolean.TRUE.equals(imageDTO.getIsPrimary())
                );

                newPlantImages.add(plantImage);
            }
            plantImageRepository.saveAll(newPlantImages);

        }
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
        List<PlantImageDTO> newImages = plantDTO.getImages();
        if(newImages != null && !newImages.isEmpty()) {
            List<PlantImage> currentImages = plantImageRepository.findByPlantId(currentPlant.getId());

        }

        if(plantDTO.getImages() != null && !plantDTO.getImages().isEmpty()) {}
        plantRepository.save(currentPlant);

    }


}

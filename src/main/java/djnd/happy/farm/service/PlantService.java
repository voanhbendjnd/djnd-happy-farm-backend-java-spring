package djnd.happy.farm.service;

import djnd.happy.farm.domain.Plant;
import djnd.happy.farm.domain.PlantImage;
import djnd.happy.farm.repository.PlantImageRepository;
import djnd.happy.farm.repository.PlantRepository;
import djnd.happy.farm.service.dto.PlantDTO;
import djnd.happy.farm.service.dto.PlantImageDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class PlantService {
    final PlantRepository  plantRepository;
    final FileService fileService;
    final PlantImageRepository plantImageRepository;
    public void createNewPlantByAdmin(PlantDTO plantDTO) throws URISyntaxException, IOException {
        String normalizedDisplayName = plantDTO.getDisplayName().trim();
        if (plantRepository.existsByDisplayNameIgnoreCase(normalizedDisplayName.toLowerCase())) {
            throw new DataConflictException(String.format("Plant with display name (%s) already exists", normalizedDisplayName), "plantManagement", "dataconflict");
        }
        Plant plant = new Plant();

        if(plantDTO.getScientificName() != null){
            String normalizedScientificName = plantDTO.getScientificName().trim();
            if(plantRepository.existsByScientificNameIgnoreCase(normalizedScientificName.toLowerCase())){
                throw new DataConflictException(String.format("Plant with scientific name (%s) already exists", normalizedScientificName), "plantManagement", "dataconflict");
            }
            plant.setScientificName(normalizedScientificName);
        }

        plant.setDisplayName(normalizedDisplayName);
        plant.setDescription(plantDTO.getDescription());
        plant.setStatus(plantDTO.getStatus());
        plant.setDescriptionJson(plantDTO.getDescriptionJson());
        plant.setIsCommunity(plantDTO.getIsCommunity());
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
        String normalizedDisplayName = plantDTO.getDisplayName().trim();
        if(plantRepository.existsByDisplayNameIgnoreCaseAndIdNot(normalizedDisplayName.toLowerCase(), plantDTO.getId())) {
            throw new DataConflictException(String.format("Plant with display name (%s) already exists", normalizedDisplayName), "plantManagement", "dataconflict");
        }
        Plant currentPlant = plantRepository.findById(plantDTO.getId()).orElseThrow(() -> new DataResourceNotFoundException(String.format("Plant with ID %d not found", plantDTO.getId()), "plantManagement", "idnotfound"));

        if(plantDTO.getScientificName() != null && !plantDTO.getScientificName().isEmpty()) {
            String normalizedScientificName = plantDTO.getScientificName().trim();
            if(plantRepository.existsByScientificNameIgnoreCaseAndIdNot(normalizedScientificName.toLowerCase(Locale.ENGLISH), plantDTO.getId())){
                throw new DataConflictException(String.format("Plant with scientific name (%s) already exists", normalizedScientificName), "plantManagement", "dataconflict");
            }
            currentPlant.setScientificName(normalizedScientificName);
        }
        currentPlant.setDisplayName(normalizedDisplayName);
        currentPlant.setDescription(plantDTO.getDescription());
        currentPlant.setStatus(plantDTO.getStatus());
        currentPlant.setDescriptionJson(plantDTO.getDescriptionJson());
        currentPlant.setIsCommunity(plantDTO.getIsCommunity());
        List<PlantImageDTO> newImages = plantDTO.getImages();
        if(newImages != null && !newImages.isEmpty()) {
            List<PlantImage> currentImages = plantImageRepository.findByPlantId(currentPlant.getId());

        }

        if(plantDTO.getImages() != null && !plantDTO.getImages().isEmpty()) {}
        plantRepository.save(currentPlant);

    }

    public ResultPaginationDTO fetchAllWithName(String q, Pageable pageable){
        String normalizedQuery = "";
        if(q != null && !q.isEmpty()) {
            normalizedQuery = q.trim();
        }
        Page<Plant> page = plantRepository.fetchAllWithQuery(normalizedQuery, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());
        res.setMeta(meta);
        List<Long> currentPlantIds = page.getContent().stream().map(Plant::getId).toList();
        Map<Long,List<PlantImage>> allCurrentImageWithPlants = plantImageRepository.findByPlantIdIn(currentPlantIds).stream()
                .collect(Collectors.groupingBy(PlantImage::getPlantId));
        res.setResult(page.getContent().stream().map(plant ->{
            PlantDTO plantDTO = new PlantDTO();
            plantDTO.setId(plant.getId());
            plantDTO.setDisplayName(plant.getDisplayName());
            plantDTO.setScientificName(plant.getScientificName());
            plantDTO.setStatus(plant.getStatus());
            plantDTO.setIsCommunity(plant.getIsCommunity());
            List<PlantImage> images = allCurrentImageWithPlants.getOrDefault(plant.getId(), Collections.emptyList());
            plantDTO.setImages(images.stream().map(x ->{
                PlantImageDTO plantImageDTO = new PlantImageDTO();
                plantImageDTO.setFileName(x.getImageUrl());
                plantImageDTO.setIsPrimary(x.getIsPrimary());
                return plantImageDTO;
            }).toList());
            return plantDTO;
        }).toList());
        return res;
    }


}

package djnd.happy.farm.service;

import djnd.happy.farm.domain.PlantPart;
import djnd.happy.farm.repository.PlantPartRepository;
import djnd.happy.farm.service.dto.PlantPartDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class PlantPartService {

    final PlantPartRepository plantPartRepository;
    public void createPlantPart(PlantPartDTO plantPartDTO) {
        String normalizedName = plantPartDTO.getName().trim();
        if(plantPartRepository.checkByName(normalizedName.toLowerCase())) {
            throw new DataConflictException("Plant part with name " + normalizedName + " already exists", "plantPartManagement", "namealreadyexists");
        }
        PlantPart plantPart = new PlantPart();
        plantPart.setName(normalizedName);
        plantPart.setDescription(plantPartDTO.getDescription());
        plantPartRepository.save(plantPart);

    }

    public void updatePlantPart(PlantPartDTO plantPartDTO) {
        PlantPart plantPart = plantPartRepository.findById(plantPartDTO.getId()).orElseThrow(() -> new DataResourceNotFoundException("Plant part with ID " + plantPartDTO.getId() + " not found", "plantPartManagement", "idnotfound"));
        String normalizedName = plantPartDTO.getName().trim();
        if(plantPartRepository.checkByNameAndIdNot(normalizedName.toLowerCase(), plantPartDTO.getId())) {
            throw new DataConflictException("Plant part with name " + normalizedName + " already exists", "plantPartManagement", "namealreadyexists");
        }
        plantPart.setName(normalizedName);
        plantPart.setDescription(plantPartDTO.getDescription());
        plantPartRepository.save(plantPart);
    }

    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchAll(String queryName, Pageable pageable) {

        String normalizedName = "";
        if(queryName != null && !queryName.isEmpty()) {
            normalizedName = queryName.trim().toLowerCase();
        }
        Page<PlantPart> page = plantPartRepository.fetchAllByName(normalizedName, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotal(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(plantPart ->{
            PlantPartDTO plantPartDTO = new PlantPartDTO();
            plantPartDTO.setId(plantPart.getId());
            plantPartDTO.setName(plantPart.getName());
            plantPartDTO.setDescription(plantPart.getDescription());
            return plantPartDTO;
        }).toList());
        return res;
    }

}

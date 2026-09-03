package djnd.happy.farm.service;

import djnd.happy.farm.domain.Disease;
import djnd.happy.farm.repository.DiseaseRepository;
import djnd.happy.farm.service.dto.DiseaseDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiseaseService {
    final DiseaseRepository diseaseRepository;
    public void createDisease(DiseaseDTO dto){
        String normalizedName = dto.getName().trim();
        if(diseaseRepository.checkByNameIgnoreCase(normalizedName.toLowerCase())){
            throw new DataConflictException(String.format("Disease with name (%s) already exists", normalizedName), "diseaseManagement", "namealreadyexists");
        }
        Disease newDisease = new Disease();
        newDisease.setName(normalizedName);
        newDisease.setDescription(dto.getDescription());
        newDisease.setSeverity(dto.getSeverity());
        diseaseRepository.save(newDisease);
    }
    public void updateDisease(DiseaseDTO dto){
        String normalizedName= dto.getName().trim();
        if(diseaseRepository.checkByNameIgnoreCaseAndIdNot(normalizedName.toLowerCase(), dto.getId())){
            throw new DataConflictException(String.format("Disease with name (%s) and ID (%d) already exists", normalizedName, dto.getId()),"diseaseManagemen", "namealreadyexists");
        }
        Disease currentDisease = diseaseRepository.findById(dto.getId()).orElseThrow(() -> new DataResourceNotFoundException(String.format("Disease with ID (%d) not found", dto.getId()), "diseaseManagement", "idnotfound"));
        currentDisease.setName(normalizedName);
        currentDisease.setDescription(dto.getDescription());
        currentDisease.setSeverity(dto.getSeverity());
        diseaseRepository.save(currentDisease);
    }

    public ResultPaginationDTO fetchAll(String q, String severity, Pageable pageable){
        String normalizedName = "";
        if(q != null && !q.isEmpty()){
            normalizedName += q;
        }
        Page<Disease> page = diseaseRepository.fetchAllWithQuery(q, severity, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(
                page.getContent()
                        .stream().map(disease ->{
                            DiseaseDTO dto = new DiseaseDTO();
                                    dto.setId(disease.getId());
                            dto.setDescription(disease.getDescription());
                            dto.setSeverity(disease.getSeverity());
                            dto.setName(disease.getName());
                            return dto;
                        }).toList()

                );
        return res;
    }
}

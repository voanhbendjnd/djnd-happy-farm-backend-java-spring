package djnd.happy.farm.service;

import djnd.happy.farm.domain.Pest;
import djnd.happy.farm.domain.PestSymptom;
import djnd.happy.farm.repository.PestRepository;
import djnd.happy.farm.repository.PestSymptomRepository;
import djnd.happy.farm.service.dto.PestDTO;
import djnd.happy.farm.service.dto.PestSymptomDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import djnd.happy.farm.service.projection.PestSymptomProjection;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PestService {
    final PestRepository pestRepository;
    final PestSymptomRepository pestSymptomRepository;
    final EntityManager entityManager;
    public void createPest(PestDTO pestDTO) {
        Pest newPest = new Pest();
        if(pestDTO.getPestSymptoms() != null && !pestDTO.getPestSymptoms().isEmpty()) {
            long countRecordPestSymptoms = pestSymptomRepository.countByIdIn(pestDTO.getPestSymptoms().stream().map(PestSymptomDTO::getId).toList());
            if(countRecordPestSymptoms  != pestDTO.getPestSymptoms().size()) {
                throw new BadRequestExceptionGlobal("Some pest symptoms Ids not found!", "pestSymptomManagement", "notfoundids");
            }
            List<PestSymptom> proxies = pestDTO.getPestSymptoms().stream().map(pestSymptom -> entityManager.getReference(PestSymptom.class, pestSymptom.getId())).toList();
            newPest.getPestSymptoms().addAll(proxies);

        }
        String normalizedName = pestDTO.getName().trim();
        if(pestRepository.checkByName(normalizedName.toLowerCase(Locale.ENGLISH))) {
            throw new DataConflictException("Pest with name '" + normalizedName + "' already exists!", "pestManagement", "alreadyexists");
        }
        newPest.setName(normalizedName);
        newPest.setDescription(pestDTO.getDescription());
        pestRepository.save(newPest);
    }


    public void updatePest(PestDTO pestDTO) {
        String normalizedName = pestDTO.getName().trim();
        if(pestRepository.checkByNameAndIdNot(normalizedName.toLowerCase(), pestDTO.getId())) {
            throw new DataConflictException(String.format("Pest with name '%s' with ID '%d' already exists", pestDTO.getName(), pestDTO.getId()), "pestManagement", "alreadyeixsts");
        }
        Pest currentPest = pestRepository.findById(pestDTO.getId()).orElseThrow(() -> new DataResourceNotFoundException("Pest with ID'"+ pestDTO.getId() +"' not found!", "pestManagement", "notfound" ));
        if(pestDTO.getPestSymptoms() != null && !pestDTO.getPestSymptoms().isEmpty()) {
            long countRecordPestSymptoms = pestSymptomRepository.countByIdIn(pestDTO.getPestSymptoms().stream().map(PestSymptomDTO::getId).toList());
            if(countRecordPestSymptoms != pestDTO.getPestSymptoms().size()){
                throw new BadRequestExceptionGlobal("Some pest symptoms Ids not found!", "pestSymptomManagement", "notfoundids");
            }
            currentPest.getPestSymptoms().clear();
            List<PestSymptom> proxies = pestDTO.getPestSymptoms().stream().map(pestSymptom -> entityManager.getReference(PestSymptom.class, pestSymptom.getId())).toList();
            currentPest.getPestSymptoms().addAll(proxies);

        }
        currentPest.setDescription(pestDTO.getDescription());
        currentPest.setName(normalizedName);
        pestRepository.save(currentPest);
    }

    public ResultPaginationDTO fetchAllWithQuery(String q, Pageable pageable){
        String normalizedName = "";
        if(q != null && !q.isEmpty()) {
            normalizedName += q;
        }
        Page<Pest> page = pestRepository.fetchAllWithQuery(normalizedName, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());
        res.setMeta(meta);
        List<PestSymptomProjection> pestIdsPestSymptoms = pestSymptomRepository.fetchWithPestIds(page.getContent().stream().map(Pest::getId).toList());
        Map<Long, List<PestSymptomProjection>> mapPestSymptom = pestIdsPestSymptoms.stream().collect(Collectors.groupingBy(PestSymptomProjection::getPestId));
        res.setResult(page.getContent().stream().map(pest ->{
            PestDTO pestDTO = new PestDTO();
            pestDTO.setId(pest.getId());
            pestDTO.setName(pest.getName());
            pestDTO.setDescription(pest.getDescription());
            List<PestSymptomProjection> projectionList = mapPestSymptom.getOrDefault(pest.getId(), Collections.emptyList());
            List<PestSymptomDTO> pestSymptoms= projectionList.stream().map(
                    psProjection ->{
                        PestSymptomDTO pestSymptomDTO = new PestSymptomDTO();
                        pestSymptomDTO.setId(psProjection.getId());
                        pestSymptomDTO.setName(psProjection.getName());
                        return pestSymptomDTO;
                    }

            ).toList();
            pestDTO.setPestSymptoms(pestSymptoms);

            return pestDTO;
        }).toList());
        return res;


    }
}

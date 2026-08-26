package djnd.happy.farm.service;

import djnd.happy.farm.domain.Fertilizer;
import djnd.happy.farm.domain.GrowthStage;
import djnd.happy.farm.repository.FertilizerRepository;
import djnd.happy.farm.repository.GrowthStageRepository;
import djnd.happy.farm.service.dto.FertilizerDTO;
import djnd.happy.farm.service.dto.FertilizerGrowthStageDTO;
import djnd.happy.farm.service.dto.FertilizerSearchCriteriaDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.FertilizerAlreadyExistsException;
import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import djnd.happy.farm.service.errors.NotFoundExceptionGlobal;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FertilizerService {
    final FertilizerRepository fertilizerRepository;
    final GrowthStageRepository growthStageRepository;
    final EntityManager entityManager;
    public void createFertilizer(FertilizerGrowthStageDTO dto) {
        String normalizedName = dto.getName().trim();
        if(fertilizerRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new FertilizerAlreadyExistsException("Name fertilizer " + normalizedName + " already use in", "namealreadyexists");
        }
        Fertilizer fertilizer = new Fertilizer();
        fertilizer.setName(normalizedName);
        fertilizer.setDescription(dto.getDescription());
        fertilizer.setDescriptionJson(dto.getDescriptionJson());
        fertilizer.setType(dto.getType());
        fertilizer.setNitrogen(dto.getNitrogen());
        fertilizer.setPhosphorus(dto.getPhosphorus());
        fertilizer.setPotassium(dto.getPotassium());
        if(dto.getGrowthStageIds() != null && !dto.getGrowthStageIds().isEmpty()) {
            long countGrowthStages =  growthStageRepository.countByIdIn(dto.getGrowthStageIds());
            if(countGrowthStages != dto.getGrowthStageIds().size()) {
                throw new BadRequestExceptionGlobal("Growth stages not found", "growthStageManagement", "growstageidsnotfound");
            }
            Set<GrowthStage> proxies = dto.getGrowthStageIds().stream().map(growthStageId -> entityManager.getReference(GrowthStage.class, growthStageId)).collect(Collectors.toSet());
            fertilizer.setGrowthStages(proxies);
        }
        fertilizerRepository.save(fertilizer);
    }

    public void updateFertilizer(FertilizerGrowthStageDTO dto) {
        Fertilizer fertilizer = fertilizerRepository.findById(dto.getId()).orElseThrow(()-> new  NotFoundExceptionGlobal("Fertilizer with ID " + dto.getId() + " not found", "fertilizerManagement", "notfoudid"));
        String normalizedName = dto.getName().trim();
        fertilizer.setName(normalizedName);
        fertilizer.setDescription(dto.getDescription());
        fertilizer.setDescriptionJson(dto.getDescriptionJson());
        fertilizer.setType(dto.getType());
        fertilizer.setNitrogen(dto.getNitrogen());
        fertilizer.setPhosphorus(dto.getPhosphorus());
        fertilizer.setPotassium(dto.getPotassium());
        if(dto.getGrowthStageIds() != null && !dto.getGrowthStageIds().isEmpty()) {
            long countGrowthStages = growthStageRepository.countByIdIn(dto.getGrowthStageIds());
            if(countGrowthStages != dto.getGrowthStageIds().size()) {
                throw new BadRequestExceptionGlobal("Growth stages not found", "growthStageManagement", "growstageidsnotfound");
            }
            fertilizer.getGrowthStages().clear();
            Set<GrowthStage> proxies = dto.getGrowthStageIds().stream().map(growthStageId -> entityManager.getReference(GrowthStage.class,growthStageId)).collect(Collectors.toSet());
            fertilizer.getGrowthStages().addAll(proxies);
        }
        fertilizerRepository.save(fertilizer);

    }
    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchAll(FertilizerSearchCriteriaDTO dto, Pageable pageable){
        Page<Fertilizer> page = fertilizerRepository.fetchAll(
                dto.getName(),
                dto.getFertilizerType(),
                dto.getMinNitrogen(),
                dto.getMaxNitrogen(),
                dto.getMinPhosphorus(),
                dto.getMaxPhosphorus(),
                dto.getMinPotassium(),
                dto.getMaxPotassium(),
                dto.getGrowthStageId(), pageable
        );
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(
                entity ->{
                    FertilizerGrowthStageDTO fertilizerDTO = new FertilizerGrowthStageDTO();
                    fertilizerDTO.setId(entity.getId());
                    fertilizerDTO.setName(entity.getName());
                    fertilizerDTO.setDescription(entity.getDescription());
                    fertilizerDTO.setType(entity.getType());
                    fertilizerDTO.setNitrogen(entity.getNitrogen());
                    fertilizerDTO.setPhosphorus(entity.getPhosphorus());
                    fertilizerDTO.setPotassium(entity.getPotassium());
                    fertilizerDTO.setDescriptionJson(entity.getDescriptionJson());
                    fertilizerDTO.setGrowthStages(entity.getGrowthStages());
                    return fertilizerDTO;
                }
        ).collect(Collectors.toList())
        );
        return res;
    }

}

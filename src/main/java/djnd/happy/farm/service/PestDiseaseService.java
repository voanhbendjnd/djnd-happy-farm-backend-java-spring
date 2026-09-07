package djnd.happy.farm.service;

import djnd.happy.farm.domain.Disease;
import djnd.happy.farm.domain.PestDisease;
import djnd.happy.farm.repository.DiseaseRepository;
import djnd.happy.farm.repository.PestDiseaseRepository;
import djnd.happy.farm.repository.PestRepository;
import djnd.happy.farm.service.dto.PestDiseaseDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PestDiseaseService {
    final PestDiseaseRepository pestDiseaseRepository;
    final PestRepository pestRepository;
    final DiseaseRepository diseaseRepository;
    public void createPestDisease(PestDiseaseDTO dto){
        pestRepository.findById(dto.getPestId()).orElseThrow(()-> new DataResourceNotFoundException(String.format("Pest with ID %d not found",dto.getPestId()), "pestManagement", "notfoundid"));
        diseaseRepository.findById(dto.getDiseaseId()).orElseThrow(()-> new DataResourceNotFoundException(String.format("Disease with ID %d not found", dto.getDiseaseId()), "diseaseManagement", "notfoundid"));
        if(!pestRepository.existsById(dto.getPestId())) {
            throw new DataResourceNotFoundException(String.format("Pest with ID %d not found",dto.getPestId()), "pestManagement", "pestidnotfoundid");
        }
        if(!diseaseRepository.existsById(dto.getDiseaseId())){
            throw new DataResourceNotFoundException(String.format("Disease with ID %d not found",dto.getDiseaseId()), "diseaseManagement", "diseaseidnotfoundid");
        }
        if(pestDiseaseRepository.existsByPestIdAndDiseaseId(dto.getPestId(), dto.getDiseaseId())) {
            throw new DataConflictException(
                    "This pest-disease relationship already exists", "pestDiseaseManagement", "relationalreadyexists");
        }
        PestDisease pd = new PestDisease();
        pd.setPestId(dto.getPestId());
        pd.setDiseaseId(dto.getDiseaseId());
        pd.setPestId(dto.getPestId());
        pd.setTransmissionRole(dto.getTransmissionRole());
        pestDiseaseRepository.save(pd);
    }

    public void updatePestDisease(PestDiseaseDTO dto){
        PestDisease currentPestDisease = pestDiseaseRepository.findById(dto.getId()).orElseThrow(() -> new DataResourceNotFoundException(
                "Pest-disease relation with ID " + dto.getId() + " not found", "pestDiseaseManagement", "idnotfound"));
        if(pestDiseaseRepository.existsByPestIdAndDiseaseId(dto.getPestId(), dto.getDiseaseId())){
            throw new DataConflictException(
                    "This pest-disease relationship already exists", "pestDiseaseManagement", "relationalreadyexists");
        }
        currentPestDisease.setDescription(dto.getDescription());
        currentPestDisease.setTransmissionRole(dto.getTransmissionRole());
        pestDiseaseRepository.save(currentPestDisease);
    }

    public void deletePestDisease(Long id){
        if(!pestDiseaseRepository.existsById(id)){
            throw new DataResourceNotFoundException("Pest-disease relation with ID " + id + " not found", "pestDiseaseManagement", "idnotfound");

        }
        pestDiseaseRepository.deleteById(id);
    }

    public List<PestDiseaseDTO> fetchByPestId(Long pestId){
        List<PestDisease> relations = pestDiseaseRepository.findAllByPestId(pestId);
        if(relations.isEmpty()){
            return List.of();
        }
        List<Long> diseaseIds = relations.stream().map(PestDisease::getPestId).toList();
        Map<Long, Disease> diseaseMap = diseaseRepository.findAllById(diseaseIds).stream().collect(Collectors.toMap(Disease::getId, d -> d));
        return relations.stream().map(pd ->{
            PestDiseaseDTO dto = new PestDiseaseDTO();
            dto.setPestId(pd.getId());
            dto.setPestId(pd.getPestId());
            dto.setDiseaseId(pd.getDiseaseId());
            dto.setDescription(pd.getDescription());
            dto.setTransmissionRole(pd.getTransmissionRole());
            Disease d = diseaseMap.get(pd.getDiseaseId());
            if(d != null){
                dto.setDiseaseName(d.getName());
                dto.setDiseaseSeverity(d.getSeverity());
            }
            return dto;
        }).toList();
    }
}

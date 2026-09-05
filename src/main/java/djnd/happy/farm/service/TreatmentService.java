package djnd.happy.farm.service;

import djnd.happy.farm.domain.Treatment;
import djnd.happy.farm.repository.TreatmentRepository;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.dto.TreatmentDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TreatmentService {
    final TreatmentRepository treatmentRepository;

    public void createTreatment(TreatmentDTO dto) {
        String normalizedMethod= dto.getMethod().trim();
        if(treatmentRepository.checkByMethodIgnoreCase(normalizedMethod.toLowerCase())){
            throw new DataConflictException(String.format("Treatment with method name %s already exists", normalizedMethod), "treatmentManagement", "methodalreadyexists");
        }
        Treatment treatment = new Treatment();
        treatment.setMethod(normalizedMethod);
        treatment.setDescription(dto.getDescription());
        treatmentRepository.save(treatment);
    }

    public void updateTreatment(TreatmentDTO dto) {
        String normalizedMethod = dto.getMethod().trim();
        if(treatmentRepository.checkByMethodIgnoreCaseAndIdNot(normalizedMethod.toLowerCase(),dto.getId())){
            throw new DataConflictException(String.format("Treatment with method name %s and ID %d already exists", normalizedMethod, dto.getId()),"treatmentManagement", "methodandidalreadyexists");
        }
        Treatment currentTreatment = treatmentRepository.findById(dto.getId()).orElseThrow(() -> new DataResourceNotFoundException(String.format("Treatment with ID %d not found", dto.getId()),"treatmentManagement","idnotfound"));
        currentTreatment.setDescription(dto.getDescription());
        currentTreatment.setMethod(normalizedMethod);
        treatmentRepository.save(currentTreatment);
    }

    public ResultPaginationDTO fetchAll(String q, Pageable pageable) {
        String normalizedMethod = "";
        if(q != null && !q.isEmpty()){
            normalizedMethod += q;
        }
        Page<Treatment> page = treatmentRepository.fetchAllWithQuery(normalizedMethod.toLowerCase(Locale.ENGLISH), pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(treatment ->{
            TreatmentDTO dto = new TreatmentDTO();
            dto.setId(treatment.getId());
            dto.setMethod(treatment.getMethod());
            dto.setDescription(treatment.getDescription());
            return dto;
        }).toList());
        return res;
    }
}

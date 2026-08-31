package djnd.happy.farm.service;

import djnd.happy.farm.domain.PestSymptom;
import djnd.happy.farm.repository.PestSymptomRepository;
import djnd.happy.farm.service.dto.PestSymptomDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
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
public class PestSymptomService {
    final PestSymptomRepository pestSymptomRepository;
    public void createPestSymptom(PestSymptomDTO dto) {
        String normalizedName = dto.getName().trim();
        if(pestSymptomRepository.existByNameIgnoreCaseDB(normalizedName.trim())) {
            throw new DataConflictException("Name pest symptom with name "  +normalizedName+  " already exists", "pestSymptomManagement", "namealreadyexists");
        }
        PestSymptom pestSymptom = new PestSymptom();
        pestSymptom.setName(normalizedName);
        pestSymptom.setDescription(dto.getDescription());
        pestSymptomRepository.save(pestSymptom);
    }

    public void updatePestSymptom(PestSymptomDTO dto) {
        String normalizedName = dto.getName().trim();
        if(pestSymptomRepository.existByNameIgnoreCaseDBAndIdNot(normalizedName.toLowerCase(Locale.ENGLISH), dto.getId())) {
            throw new DataConflictException("Name pest symptom with name "  +normalizedName+  " already exists", "pestSymptomManagement", "namealreadyexists");
        }
        PestSymptom currentPestSymptom = pestSymptomRepository.findById(dto.getId()).orElseThrow(() -> new DataResourceNotFoundException("Pest symptom with ID " + dto.getId()+ "not found", "pestSymptomManagement", "idnotfound"));
        currentPestSymptom.setDescription(dto.getDescription());
        currentPestSymptom.setName(dto.getName());
        pestSymptomRepository.save(currentPestSymptom);
    }

    public void deletePestSymptomById(Long id) {}
    public ResultPaginationDTO fetchAllWithPagination(String q, Pageable pageable) {
        String query = "";
        if(q != null && !q.isEmpty()) {
            query += q.trim().toLowerCase(Locale.ENGLISH);
        }
        Page<PestSymptom> page = pestSymptomRepository.fetchAllWithQueryAndPagination(query, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(ps ->{
            var dto = new PestSymptomDTO();
            dto.setId(ps.getId());
            dto.setName(ps.getName());
            dto.setDescription(ps.getDescription());
            return dto;
        }).toList());
        return res;
    }


}

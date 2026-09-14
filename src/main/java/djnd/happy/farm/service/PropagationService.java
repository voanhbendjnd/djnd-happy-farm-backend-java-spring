package djnd.happy.farm.service;

import djnd.happy.farm.domain.Propagation;
import djnd.happy.farm.repository.PropagationRepository;
import djnd.happy.farm.service.dto.PropagationDTO;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.DataConflictException;
import djnd.happy.farm.service.errors.DataResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PropagationService {
    final PropagationRepository propagationRepository;

    public void create(PropagationDTO dto) {
        String normalizedMethod = dto.getMethod().trim();
        if(propagationRepository.existsByMethodIgnoreCase(normalizedMethod.toLowerCase(Locale.ENGLISH))) {
            throw new DataConflictException(String.format("Propagation with method %s already exists", normalizedMethod), "propagationManagement", "methodconflict");
        }
        Propagation propagation = new Propagation();
        propagation.setMethod(normalizedMethod);
        propagation.setDifficulty(dto.getDifficulty());
        propagation.setDescription(dto.getDescription());
        propagationRepository.save(propagation);
    }

    public void update(PropagationDTO dto) {
        String normalizedMethod = dto.getMethod().trim();

        if(propagationRepository.existsByMethodIgnoreCaseAndIdNot(normalizedMethod.toLowerCase(Locale.ENGLISH), dto.getId())) {
            throw new DataConflictException(String.format("Propagation with method %s and ID %d already exists", normalizedMethod, dto.getId()), "propagationManagement", "methodandidconflict");
        }
        Propagation currentPropagation = propagationRepository
                .findById(dto.getId()).orElseThrow(()-> new DataResourceNotFoundException(String.format("Propagation with ID %d not found", dto.getId()), "propagationManagement", "idnotfound"));
        currentPropagation.setDifficulty(dto.getDifficulty());
        currentPropagation.setDescription(dto.getDescription());
        currentPropagation.setMethod(normalizedMethod);
        propagationRepository.save(currentPropagation);
    }


    public ResultPaginationDTO fetchAllWithMethod(String method, String difficulty, Pageable pageable) {
        Specification<Propagation> ps =(root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(method != null && !method.isEmpty()){
                predicates.add(cb.like(cb.lower(root.get("method")), "%" + method.toLowerCase(Locale.ENGLISH) + "%"));
            }
            if(difficulty != null){
                predicates.add(cb.equal(root.get("difficulty"), difficulty));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        ResultPaginationDTO res = new ResultPaginationDTO();
        Page<Propagation> page = propagationRepository.findAll(ps, pageable);
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent().stream().map(pro ->{
            PropagationDTO dto = new PropagationDTO();
            dto.setId(pro.getId());
            dto.setDescription(pro.getDescription());
            dto.setDifficulty(pro.getDifficulty());
            dto.setMethod(pro.getMethod());
            return dto;
                }

        ).toList());


        return res;
    }
}

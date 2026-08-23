package djnd.happy.farm.service;

import djnd.happy.farm.domain.Habitat;
import djnd.happy.farm.repository.HabitatRepository;
import djnd.happy.farm.service.dto.ResultPaginationDTO;
import djnd.happy.farm.service.errors.HabitatAlreadyExistsException;
import djnd.happy.farm.service.errors.HabitatNotFoundException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
@Transactional
public class HabitatService {
    final HabitatRepository habitatRepository;

    public void createHabitat(Habitat request) {

        if(habitatRepository.existByName(request.getName().trim().toLowerCase(Locale.ENGLISH))) {
            throw new HabitatAlreadyExistsException("Habitat with name " + request.getName() + " already in use", "habitatManagement", "conflictname");
        }
        habitatRepository.save(request);
    }

    public void updateHabitat(Habitat request) {
        Habitat habitatExisting = habitatRepository.findByNameIgnoreCase(request.getName().trim()).orElseThrow(() -> new HabitatNotFoundException("Habitat name " + request.getName() + " not found", "habitatManagement", "notfoundname"));
        habitatExisting.setDescription(request.getDescription());
        habitatRepository.save(habitatExisting);
    }
    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchAll(String qName, Pageable pageable) {
        Page<Habitat> page = habitatRepository.fetchAll(qName.trim().toLowerCase(Locale.ENGLISH), pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        var meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        res.setResult(page.getContent());
        return res;
    }

}

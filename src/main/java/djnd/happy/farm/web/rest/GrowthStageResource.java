package djnd.happy.farm.web.rest;

import djnd.happy.farm.domain.GrowthStage;
import djnd.happy.farm.repository.GrowthStageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/growthStages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowthStageResource {
    final GrowthStageRepository growthStageRepository;

    @GetMapping
    public ResponseEntity<List<GrowthStage>> getAllGrowthStages() {
        return ResponseEntity.ok(growthStageRepository.findAll());
    }
}

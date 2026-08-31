package djnd.happy.farm.service;

import djnd.happy.farm.repository.PestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PestService {
    final PestRepository pestRepository;


    public void createPest(){

    }
}

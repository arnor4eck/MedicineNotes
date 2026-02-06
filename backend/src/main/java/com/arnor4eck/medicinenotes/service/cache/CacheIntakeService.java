package com.arnor4eck.medicinenotes.service.cache;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CacheIntakeService {

    private final IntakeRepository intakeRepository;

    @Cacheable(cacheNames = "intake", key = "#id", unless = "#result == null")
    public Optional<Intake> getIntakeById(long id){
        return intakeRepository.findById(id);
    }
}

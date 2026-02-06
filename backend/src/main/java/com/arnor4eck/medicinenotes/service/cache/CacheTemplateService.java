package com.arnor4eck.medicinenotes.service.cache;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CacheTemplateService {

    private final TemplateRepository templateRepository;

    @Cacheable(cacheNames = "template", key = "#id", unless = "#result == null")
    public Optional<MedicineTemplate> getTemplateById(long id) {
        return templateRepository.findById(id);
    }
}

package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.config.LimitsConfig;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.util.dto.MedicineTemplateDto;
import com.arnor4eck.medicinenotes.util.exception.illegal_argument.LimitExceededException;
import com.arnor4eck.medicinenotes.util.exception.not_found.MedicineTemplateNotFoundException;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineTemplateService {

    private final TemplateRepository templateRepository;

    private final UserDetailsService userDetailsService;

    private final LimitsConfig limitsConfig;

    public MedicineTemplate getTemplateByIdCreator(long id,
                                                   String email) {
        MedicineTemplate template = templateRepository.findById(id)
                .orElseThrow(() ->new MedicineTemplateNotFoundException("Шаблон с заданным ID не найден."));

        if(!template.getCreator().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),
                    "У вас нет доступа к этому ресурсу.");

        return template;
    }

    public void create(CreateTemplateRequest request,
                       String creatorEmail) {

        if(request.quantityPerDay() > limitsConfig.getMaxTimesADay())
            throw new LimitExceededException("Максимум %s раз в день."
                            .formatted(limitsConfig.getMaxTimesADay()));

        if(request.until().isAfter(LocalDate.now().plusDays(limitsConfig.getMaxDuration())))
            throw new LimitExceededException("Максимальная продолжительность - %s дней."
                            .formatted(limitsConfig.getMaxDuration()));

        if(templateRepository.countAllTemplatesByCreatorEmail(creatorEmail) > limitsConfig.getMaxTemplates())
            throw new LimitExceededException("Максимум можно создать %s шаблонов."
                            .formatted(limitsConfig.getMaxTemplates()));

        User creator = (User) userDetailsService.loadUserByUsername(creatorEmail);

        templateRepository.save(MedicineTemplate.builder()
                .name(request.name())
                .description(request.description())
                .quantityPerDay(request.quantityPerDay())
                .until(request.until())
                .creator(creator)
                .build());
    }

    public Collection<MedicineTemplateDto> getAllUserTemplates(String email) {
        return templateRepository.findByCreatorEmail(email)
                .stream().map(MedicineTemplateDto::getMedicineTemplateDto)
                .collect(Collectors.toUnmodifiableList());
    }
}

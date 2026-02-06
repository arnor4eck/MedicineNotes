package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.config.LimitsProperties;
import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.service.cache.CacheTemplateService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicineTemplateService {

    private final TemplateRepository templateRepository;

    private final IntakeRepository intakeRepository;

    private final UserDetailsService userDetailsService;

    private final LimitsProperties limitsProperties;

    private final CacheTemplateService cacheTemplateService;

    public MedicineTemplate getTemplateByIdCreator(long id,
                                                   String email) {
        MedicineTemplate template = cacheTemplateService.getTemplateById(id)
                .orElseThrow(() ->new MedicineTemplateNotFoundException("Шаблон с заданным ID не найден."));

        if(!template.getCreator().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),
                    "У вас нет доступа к этому ресурсу.");

        return template;
    }

    public MedicineTemplate create(CreateTemplateRequest request,
                       String creatorEmail) {

        if (!request.until().isAfter(LocalDate.now()))
            throw new IllegalArgumentException(
                    "Дата 'до' должна быть в будущем. " +
                            request.until() + " <= " + LocalDate.now()
            );

        if(request.quantityPerDay() > limitsProperties.getMaxTimesADay())
            throw new LimitExceededException("Максимум %s раз в день."
                            .formatted(limitsProperties.getMaxTimesADay()));

        if(request.until().isAfter(LocalDate.now().plusDays(limitsProperties.getMaxDuration())))
            throw new LimitExceededException("Максимальная продолжительность - %s дней."
                            .formatted(limitsProperties.getMaxDuration()));

        if(templateRepository.countAllTemplatesByCreatorEmail(creatorEmail) > limitsProperties.getMaxTemplates())
            throw new LimitExceededException("Максимум можно создать %s шаблонов."
                            .formatted(limitsProperties.getMaxTemplates()));

        User creator = (User) userDetailsService.loadUserByUsername(creatorEmail);

        MedicineTemplate template = templateRepository.save(MedicineTemplate.builder()
                .name(request.name())
                .description(request.description())
                .quantityPerDay(request.quantityPerDay())
                .until(request.until())
                .creator(creator)
                .build());

        // TODO проработать систему с отрезочным созданием через Scheduler
        List<LocalDate> datesOfIntakes = LocalDate.now().datesUntil(request.until()).toList();
        List<Intake> futureIntakes = new ArrayList<>(datesOfIntakes.size() * request.quantityPerDay());

        for(LocalDate date : datesOfIntakes)
            for(int i = 0; i < request.quantityPerDay(); i++)
                futureIntakes.add(
                    Intake.builder()
                            .template(template)
                            .adoptedIn(null)
                            .status(IntakesStatus.PENDING)
                            .shouldAdoptedIn(date)
                            .build());

        intakeRepository.saveAll(futureIntakes);

        return template;
    }

    public Collection<MedicineTemplateDto> getAllUserTemplates(String email) {
        return templateRepository.findByCreatorEmail(email)
                .stream().map(MedicineTemplateDto::fromEntity)
                .toList();
    }
}

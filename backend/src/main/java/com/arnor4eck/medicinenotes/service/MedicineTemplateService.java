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
import com.arnor4eck.medicinenotes.util.request.ChangeTemplateUntilDateRequest;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    //private final IntakeService intakeService;

    public MedicineTemplate getTemplateByIdCreatorValidation(long id,
                                                   String email) {
        MedicineTemplate template = this.getTemplateById(id);
        if(!template.getCreator().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),
                    "У вас нет доступа к этому ресурсу.");

        return template;
    }

    /** Получение шаблона по ID
     * @param id ID шаблона
     * @return Найденный шаблон
     * @throws MedicineTemplateNotFoundException
     * */
    MedicineTemplate getTemplateById(long id){
        return cacheTemplateService.getTemplateById(id)
                .orElseThrow(() -> new MedicineTemplateNotFoundException("Шаблон с заданным ID не найден."));
    }

    /** Обновление существующего шаблона, изменение даты окончания приёмов
     * @param id ID шаблона
     * @param email Email пользователя, запрашивающего смену
     * @param changeTemplateUntilDateRequest Новая дата окончания приёмов
     *
     * */
    @Transactional
    public void changeTemplateUntilDateById(long id, String email,
                                            ChangeTemplateUntilDateRequest changeTemplateUntilDateRequest){
        MedicineTemplate template = this.getTemplateByIdCreatorValidation(id, email);

        if(!this.changeTemplateUntilDate(template, changeTemplateUntilDateRequest.until()))
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),
                    "Некорректная дата.");

        LocalDate oldUntilTime = template.getUntil();
        template.setUntil(changeTemplateUntilDateRequest.until());
        templateRepository.save(template);

        //intakeService.changeIntakesByChangingUntilTemplateDate(template.getId(), oldUntilTime);
    }

    /** Логичка валидирования коррекности смены даты окончания приёмов
     * @param template Шаблон, окончание которого меняется
     * @param newUntil Новая дата окончания
     * @return boolean
     * */
    private boolean changeTemplateUntilDate(MedicineTemplate template,
                                            LocalDate newUntil) {
        long newPeriod = ChronoUnit.DAYS.between(template.getStart(), newUntil);

        if(newPeriod <= 0) return false; // Если дата окончания раньше или равна дате начала
        if(newPeriod > limitsProperties.getMaxDuration()) return false; // Длительность приёмов больше максимально возможного количества

        return true;
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

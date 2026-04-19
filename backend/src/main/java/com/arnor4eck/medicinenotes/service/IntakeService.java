package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.service.cache.CacheIntakeService;
import com.arnor4eck.medicinenotes.util.dto.IntakeDto;
import com.arnor4eck.medicinenotes.util.exception.not_found.IntakeNotFoundException;
import com.arnor4eck.medicinenotes.util.request.ChangeIntakeStatusRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class IntakeService {

    private final IntakeRepository intakeRepository;

    private final MedicineTemplateService templateService;

    private final CacheIntakeService cacheIntakeService;

    public IntakeDto getIntakeByIdWithCreatorCheck(long id,
                                   String email){
        Intake intake = getIntakeById(id);

        checkIsIntakeOwner(intake, email);

        return IntakeDto.fromEntity(intake);
    }

    public Collection<IntakeDto> getAllIntakesByCreatorAndAdoptedDate(String email,
                                                                   LocalDate date){
        Stream<Intake> intakes = intakeRepository.findAllByCreatorEmail(email).stream();

        if(date != null)
            intakes = intakes
                    .filter(intake -> intake.getShouldAdoptedIn() != null &&
                            intake.getShouldAdoptedIn().equals(date));

        return intakes
                .map(IntakeDto::fromEntity)
                .toList();
    }

    void changeIntakesByChangingUntilTemplateDate(long templateId,
                                                  LocalDate oldUntilDate){
        MedicineTemplate template = templateService.getTemplateById(templateId);

        int compared = oldUntilDate.compareTo(template.getUntil());

        if(compared < 0) // новая дата больше
            intakeRepository.saveAll( // значит добавляем новые
                oldUntilDate
                    .datesUntil(template.getUntil())
                    .flatMap(date ->
                            IntStream.range(0, (int) template.getQuantityPerDay()) // колиичество в день
                                .mapToObj(i ->
                                    Intake.builder()
                                    .template(template)
                                    .adoptedIn(null)
                                    .status(IntakesStatus.PENDING)
                                    .shouldAdoptedIn(date)
                                    .build()
                                )
                    ).toList());
        else if(compared > 0) // новая дата меньше
            intakeRepository.deleteIntakesMoreThanNewUntil(template.getUntil(), template.getId());
    }

    public IntakeDto changeIntakeStatus(long id,
                                         ChangeIntakeStatusRequest request,
                                         String email){
        Intake intake = getIntakeById(id);
        IntakesStatus status = IntakesStatus.valueOf(request.status());

        if(intake.getStatus() == status ||
                intake.getStatus() != IntakesStatus.PENDING)
            return IntakeDto.fromEntity(intake);

        checkIsIntakeOwner(intake, email);

        intake.setStatus(status);

        if(status == IntakesStatus.DONE)
            intake.setAdoptedIn(LocalDateTime.now(ZoneId.of("UTC")));

        intakeRepository.save(intake);

        return IntakeDto.fromEntity(intake);
    }

    private Intake getIntakeById(long id){
        return cacheIntakeService.getIntakeById(id)
                .orElseThrow(() -> new IntakeNotFoundException("Факта приема препарата с заданным ID не найдено"));
    }

    private void checkIsIntakeOwner(Intake intake,
                                    String email){
        if(!intake.getTemplate().getCreator().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),
                    "У вас нет доступа к этому ресурсу.");

    }
}

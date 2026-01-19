package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.util.dto.IntakeDto;
import com.arnor4eck.medicinenotes.util.exception.not_found.IntakeNotFoundException;
import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IntakeService {

    private final IntakeRepository intakeRepository;

    public IntakeDto getIntakeByIdWithCreator(long id,
                                   String email){
        Intake intake = intakeRepository.findById(id)
                .orElseThrow(() -> new IntakeNotFoundException("Факта приема препарата с заданным ID не найдено"));

        if(!intake.getTemplate().getCreator().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),
                    "У вас нет доступа к этому ресурсу.");

        return IntakeDto.fromEntity(intake);
    }

    public Collection<IntakeDto> getAllIntakesByCreatorAndAdoptedDate(String email,
                                                                   LocalDate date){
        Collection<Intake> intakes = intakeRepository.findAllByCreatorEmail(email);

        if(date != null)
            intakes = intakes.stream()
                    .filter(intake -> intake.getAdoptedIn().toLocalDate().equals(date))
                    .collect(Collectors.toUnmodifiableList());

        return intakes.stream()
                .map(IntakeDto::fromEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    public ExceptionResponse changeIntakeStatus(long id, String status, String email){
        ExceptionResponse response;

        if(intakeRepository.setStatus(id, status, email) == 1)
            response = new ExceptionResponse(HttpStatus.ACCEPTED.value(), "Статус приёма успешно изменён");
        else
            response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Не удалось изменить статус этого приёма.");
        return response;
    }
}

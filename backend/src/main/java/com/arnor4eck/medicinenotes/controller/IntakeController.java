package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.IntakeService;
import com.arnor4eck.medicinenotes.util.dto.IntakeDto;
import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import com.arnor4eck.medicinenotes.util.validation.Status;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/intake")
@AllArgsConstructor
public class IntakeController {

    private final IntakeService intakeService;

    @GetMapping("/{id}")
    public IntakeDto getIntakeById(@PathVariable long id,
                                   @AuthenticationPrincipal String email) {
        return intakeService.getIntakeByIdWithCreator(id, email);
    }

    @GetMapping("/my")
    public Collection<IntakeDto> getAllIntakesByCreatorEmail(@AuthenticationPrincipal String email,
                                                          @RequestParam(value = "date", required = false) LocalDate date){
        return intakeService.getAllIntakesByCreatorAndAdoptedDate(email, date);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ExceptionResponse> changeStatus(@PathVariable long id,
                                       @Status @RequestParam("status") String status,
                                       @AuthenticationPrincipal String email){
        ExceptionResponse done = intakeService.changeIntakeStatus(id, status, email);
        return ResponseEntity.status(done.code()).body(done);
    }
}


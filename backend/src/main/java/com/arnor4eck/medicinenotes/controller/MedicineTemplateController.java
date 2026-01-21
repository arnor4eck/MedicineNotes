package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.util.dto.MedicineTemplateDto;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/template")
public class MedicineTemplateController {

    private final MedicineTemplateService templateService;

    @GetMapping("/{id}")
    public MedicineTemplateDto getTemplateById(@PathVariable long id,
                                               @AuthenticationPrincipal String email) {
        return MedicineTemplateDto
                .fromEntity(
                        templateService.getTemplateByIdCreator(id, email));
    }

    @PostMapping("/create")
    public ResponseEntity<MedicineTemplateDto> createTemplate(@RequestBody @Valid CreateTemplateRequest request,
                                               @AuthenticationPrincipal String email){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MedicineTemplateDto
                        .fromEntity(templateService.create(request, email)));
    }

    @GetMapping("/my")
    public Collection<MedicineTemplateDto> getTemplates(@AuthenticationPrincipal String email){
        return templateService.getAllUserTemplates(email);
    }
}

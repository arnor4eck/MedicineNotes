package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.util.dto.MedicineTemplateDto;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/template")
public class MedicineTemplateController {

    private final MedicineTemplateService templateService;

    @GetMapping("/{id}")
    public MedicineTemplateDto getTemplateById(@PathVariable long id,
                                               @AuthenticationPrincipal String email) {
        return MedicineTemplateDto
                .getMedicineTemplateDto(
                        templateService.getTemplateByIdCreator(id, email));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTemplate(@RequestBody @Valid CreateTemplateRequest request,
                                               @AuthenticationPrincipal String email){
        templateService.create(request, email);
    }
}

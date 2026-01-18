package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.util.dto.MedicineTemplateDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

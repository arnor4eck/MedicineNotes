package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.util.exception.MedicineTemplateNotFoundException;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class MedicineTemplateService {

    private final TemplateRepository templateRepository;

    private final UserDetailsService userDetailsService;

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
        User creator = (User) userDetailsService.loadUserByUsername(creatorEmail);

        templateRepository.save(MedicineTemplate.builder()
                .name(request.name())
                .description(request.description())
                .quantityPerDay(request.quantityPerDay())
                .until(request.until())
                .creator(creator)
                .build());
    }
}

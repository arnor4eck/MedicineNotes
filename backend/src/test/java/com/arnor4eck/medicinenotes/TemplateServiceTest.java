package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.config.LimitsProperties;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.service.IntakeService;
import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.service.cache.CacheTemplateService;
import com.arnor4eck.medicinenotes.util.request.ChangeTemplateUntilDateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TemplateServiceTest {

    @InjectMocks
    private MedicineTemplateService medicineTemplateService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private IntakeService intakeService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private LimitsProperties limitsProperties;

    @Mock
    private CacheTemplateService cacheTemplateService;

    @Test
    public void testChangeUntilDateCorrectNewDate() {
        User creator = new User(1L, "username", "email@mail.ru",
                "password", Role.USER);

        MedicineTemplate medicineTemplate = new MedicineTemplate(1L, "name", "desc",
                LocalDate.of(2000, 5, 5),
                LocalDate.of(2000, 5, 20),
                5, creator);
        LocalDate newDate = LocalDate.of(2000, 5, 10);

        setUpLimits();
        when(cacheTemplateService.getTemplateById(1L)).thenReturn(Optional.of(medicineTemplate));

        medicineTemplateService.changeTemplateUntilDateById(1L, creator.getEmail(),
                new ChangeTemplateUntilDateRequest(newDate));

        Assertions.assertEquals(newDate, medicineTemplate.getUntil());
    }

    @Test
    public void testChangeUntilDateMoreThenMaxDuration() {
        User creator = new User(1L, "username", "email@mail.ru",
                "password", Role.USER);

        MedicineTemplate medicineTemplate = new MedicineTemplate(1L, "name", "desc",
                LocalDate.of(2000, 5, 5),
                LocalDate.of(2000, 5, 20),
                5, creator);
        LocalDate newDate = LocalDate.MAX;

        when(cacheTemplateService.getTemplateById(1L)).thenReturn(Optional.of(medicineTemplate));
        setUpLimits();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            medicineTemplateService.changeTemplateUntilDateById(1L, creator.getEmail(),
                    new ChangeTemplateUntilDateRequest(newDate));
        });

        Assertions.assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    @Test
    public void testChangeUntilDateSetStartDate() {
        User creator = new User(1L, "username", "email@mail.ru",
                "password", Role.USER);

        MedicineTemplate medicineTemplate = new MedicineTemplate(1L, "name", "desc",
                LocalDate.of(2000, 5, 5),
                LocalDate.of(2000, 5, 20),
                5, creator);
        LocalDate newDate = medicineTemplate.getStart();

        when(cacheTemplateService.getTemplateById(1L)).thenReturn(Optional.of(medicineTemplate));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            medicineTemplateService.changeTemplateUntilDateById(1L, creator.getEmail(),
                    new ChangeTemplateUntilDateRequest(newDate));
        });

        Assertions.assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    @Test
    public void testChangeUntilDateLessThenStart() {
        User creator = new User(1L, "username", "email@mail.ru",
                "password", Role.USER);

        MedicineTemplate medicineTemplate = new MedicineTemplate(1L, "name", "desc",
                LocalDate.of(2000, 5, 5),
                LocalDate.of(2000, 5, 20),
                5, creator);
        LocalDate newDate = LocalDate.MIN;

        when(cacheTemplateService.getTemplateById(1L)).thenReturn(Optional.of(medicineTemplate));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            medicineTemplateService.changeTemplateUntilDateById(1L, creator.getEmail(),
                    new ChangeTemplateUntilDateRequest(newDate));
        });

        Assertions.assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    private void setUpLimits(){
        when(limitsProperties.getMaxDuration()).thenReturn((short) 90);
    }
}

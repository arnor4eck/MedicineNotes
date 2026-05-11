package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.config.LimitsProperties;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import com.arnor4eck.medicinenotes.service.IntakeService;
import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.service.cache.CacheTemplateService;
import com.arnor4eck.medicinenotes.util.exception.illegal_argument.LimitExceededException;
import com.arnor4eck.medicinenotes.util.request.CreateTemplateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
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
    private IntakeRepository intakeRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private LimitsProperties limitsProperties;

    @Mock
    private CacheTemplateService cacheTemplateService;

    @Test
    public void testCreateTemplateCorrect() {
        User creator = createUser();
        CreateTemplateRequest request = createRequest(2,
                LocalDate.of(2125, 5, 5), LocalDate.of(2125, 5, 30));

        setUpLimits();
        when(userDetailsService.loadUserByUsername(creator.getEmail())).thenReturn(creator);
        when(templateRepository.save(any(MedicineTemplate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(templateRepository.countAllTemplatesByCreatorEmail(creator.getEmail())).thenReturn(0L);

        MedicineTemplate template = medicineTemplateService.create(request, creator.getEmail());

        Assertions.assertNotNull(template);
        Assertions.assertEquals(template.getCreator(), creator);
    }

    @Test
    public void testCreateTemplateStartDateAfterEndDate() {
        User creator = createUser();

        CreateTemplateRequest request = createRequest(2,
                LocalDate.of(2125, 5, 15),
                LocalDate.MIN);

        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            medicineTemplateService.create(request, creator.getEmail());
        });

        Assertions.assertTrue(ex.getMessage().startsWith("Дата 'до' должна быть в будущем."));
    }

    @Test
    public void testCreateTemplateStartDayBeforeCurrentDate() {
        User creator = createUser();

        CreateTemplateRequest request = createRequest(2,
                LocalDate.of(2000, 5, 15),
                LocalDate.MIN);

        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            medicineTemplateService.create(request, creator.getEmail());
        });

        Assertions.assertEquals("Дата начала не должна быть раньше сегодняшней даты.",
                ex.getMessage());
    }

    @Test
    public void testCreateTemplateReachedQuantityPerDayLimit() {
        User creator = createUser();

        CreateTemplateRequest request = createRequest(10000,
                LocalDate.of(2125, 5, 5),
                LocalDate.of(2125, 5, 15));

        LimitExceededException ex = Assertions.assertThrows(LimitExceededException.class, () -> {
            medicineTemplateService.create(request, creator.getEmail());
        });

        Assertions.assertTrue(ex.getMessage().startsWith("Максимум"));
        Assertions.assertTrue(ex.getMessage().endsWith("раз в день."));
    }

    @Test
    public void testCreateTemplateDurationLimit() {
        User creator = createUser();

        CreateTemplateRequest request = createRequest(2,
                LocalDate.of(2125, 5, 15),
                LocalDate.MAX);

        setUpMaxTimesADay();
        setUpMaxDuration();
        LimitExceededException ex = Assertions.assertThrows(LimitExceededException.class, () -> {
            medicineTemplateService.create(request, creator.getEmail());
        });

        Assertions.assertTrue(ex.getMessage().startsWith("Максимальная продолжительность"));
        Assertions.assertTrue(ex.getMessage().endsWith("дней."));
    }

    @Test
    public void testCreateTemplateMaxTemplates() {
        User creator = createUser();

        CreateTemplateRequest request = createRequest(2,
                LocalDate.of(2125, 5, 10),
                LocalDate.of(2125, 5, 12));

        setUpLimits();
        when(templateRepository.countAllTemplatesByCreatorEmail(creator.getEmail())).thenReturn(Long.MAX_VALUE);

        LimitExceededException ex = Assertions.assertThrows(LimitExceededException.class, () -> {
            medicineTemplateService.create(request, creator.getEmail());
        });

        Assertions.assertTrue(ex.getMessage().startsWith("Максимум можно создать"));
        Assertions.assertTrue(ex.getMessage().endsWith("шаблонов."));
    }

    private void setUpLimits(){
        setUpMaxDuration();
        setUpMaxTimesADay();
        setUpMaxTemplates();
    }

    private void setUpMaxDuration(){
        when(limitsProperties.getMaxDuration()).thenReturn((short) 90);
    }

    private void setUpMaxTimesADay(){
        when(limitsProperties.getMaxTimesADay()).thenReturn((short) 10);
    }

    private void setUpMaxTemplates(){
        when(limitsProperties.getMaxTemplates()).thenReturn((short) 15);
    }

    private User createUser(){
        return new User(1L, "username", "email@mail.ru",
                "password", Role.USER);
    }

    private CreateTemplateRequest createRequest(int quantity, LocalDate start, LocalDate finish){
        return new CreateTemplateRequest("template", "desc", quantity, start, finish);
    }
}

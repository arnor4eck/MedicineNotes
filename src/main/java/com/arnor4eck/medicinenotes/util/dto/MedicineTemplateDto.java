package com.arnor4eck.medicinenotes.util.dto;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;

import java.time.LocalDate;

public record MedicineTemplateDto(long id, String name,
                                  String description, long quantityPerDay,
                                  LocalDate until) {

    public static MedicineTemplateDto getMedicineTemplateDto(MedicineTemplate medicineTemplate) {
        return new MedicineTemplateDto(medicineTemplate.getId(),
                medicineTemplate.getName(),
                medicineTemplate.getDescription(),
                medicineTemplate.getQuantityPerDay(),
                medicineTemplate.getUntil());
    }
}

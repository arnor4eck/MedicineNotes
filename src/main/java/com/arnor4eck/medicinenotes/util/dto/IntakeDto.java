package com.arnor4eck.medicinenotes.util.dto;

import com.arnor4eck.medicinenotes.entity.Intake;

import java.time.LocalDateTime;

public record IntakeDto(long id, String name,
                        LocalDateTime adoptedIn, String status) {
    public static IntakeDto fromEntity(Intake entity) {
        return new IntakeDto(entity.getId(),
                            entity.getTemplate().getName(),
                            entity.getAdoptedIn(),
                            entity.getStatus().toString());
    }
}

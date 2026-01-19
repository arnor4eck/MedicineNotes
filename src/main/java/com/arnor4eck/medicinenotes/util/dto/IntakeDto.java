package com.arnor4eck.medicinenotes.util.dto;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record IntakeDto(long id, String name,
                        String adoptedIn,
                        String status) {

    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static IntakeDto fromEntity(Intake entity) {

        return new IntakeDto(entity.getId(),
                            entity.getTemplate().getName(),
                            entity.getAdoptedIn().toInstant(ZoneOffset.UTC)
                            .atZone(ZoneId.of("Europe/Moscow")).format(dateTimeFormatter),
                            entity.getStatus().toString());
    }
}

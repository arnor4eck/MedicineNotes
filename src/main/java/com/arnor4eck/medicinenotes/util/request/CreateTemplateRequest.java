package com.arnor4eck.medicinenotes.util.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateTemplateRequest(@NotBlank String name, @NotBlank String description,
                                    @Positive int quantityPerDay,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate until) {
}

package com.arnor4eck.medicinenotes.util.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ChangeTemplateUntilDateRequest(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate until) {}

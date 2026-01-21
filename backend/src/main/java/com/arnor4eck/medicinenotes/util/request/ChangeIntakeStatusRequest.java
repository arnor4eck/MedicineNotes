package com.arnor4eck.medicinenotes.util.request;

import com.arnor4eck.medicinenotes.util.validation.Status;

public record ChangeIntakeStatusRequest(@Status String status) {}

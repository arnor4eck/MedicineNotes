package com.arnor4eck.medicinenotes.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "limits")
public class LimitsConfig {
    private short maxTemplates;
    private short maxDuration;
    private short maxTimesADay;
}

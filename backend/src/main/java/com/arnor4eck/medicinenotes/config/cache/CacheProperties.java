package com.arnor4eck.medicinenotes.config.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "cache")
@Setter
@Getter
public class CacheProperties {
    private List<String> names;
}

package org.themoviedb.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="org.themoviedb.api")
public class ServiceProps {
    private String baseUri;
    private String apiKey;
}

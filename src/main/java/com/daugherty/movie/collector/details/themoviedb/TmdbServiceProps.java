package com.daugherty.movie.collector.details.themoviedb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="org.themoviedb.api")
public class TmdbServiceProps {
    private String baseUri;
    private String apiKey;
}

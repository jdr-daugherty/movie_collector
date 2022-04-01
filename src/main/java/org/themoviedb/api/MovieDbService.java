package org.themoviedb.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.themoviedb.api.dta.TmdbMovie;
import org.themoviedb.api.dta.TmdbMovieList;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieDbService {

    private final ServiceProps props;

    @Bean
    private RestTemplate rest() {
        return new RestTemplate();
    }

    public Optional<TmdbMovie> getMovieById(long id) {
        ResponseEntity<TmdbMovie> entity =
                rest().getForEntity(movieByIdUri(id), TmdbMovie.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(entity.getBody());
        }
        return Optional.empty();
    }

    public List<TmdbMovie> findMovies(String query) {
        try {
            ResponseEntity<TmdbMovieList> entity =
                    rest().getForEntity(moviesByQueryUri(query), TmdbMovieList.class);

            if (entity.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(entity.getBody()).getResults();
            }
        } catch (URISyntaxException e) {
            log.error("Query URI creation failed for '" + query + "'", e);
        }

        return List.of();
    }

    public Optional<TmdbMovie> getMovieByTitle(String title) {
        return findMovies(title).stream()
                .filter(m -> m.getTitle().equals(title))
                .max(Comparator.comparing(TmdbMovie::getReleaseDate));
    }

    private URI movieByIdUri(long id) {
        return URI.create(String.format("%s/movie/%d?api_key=%s",
                props.getBaseUri(), id, props.getApiKey()));
    }

    private URI moviesByQueryUri(String query) throws URISyntaxException {
        final URI base = URI.create(props.getBaseUri());
        return new URI(base.getScheme(), base.getUserInfo(), base.getHost(), base.getPort(),
                base.getPath() + "/search/movie",
                String.format("api_key=%s&query=%s", props.getApiKey(), query),
                base.getFragment());
    }
}

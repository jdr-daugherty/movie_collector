package com.daugherty.movie.collector.details.themoviedb;

import com.daugherty.movie.collector.details.MovieDetails;
import com.daugherty.movie.collector.details.MovieDetailsService;
import com.daugherty.movie.collector.details.themoviedb.dto.TmdbMovie;
import com.daugherty.movie.collector.details.themoviedb.dto.TmdbMovieList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(value = {"org.themoviedb.api.base_uri", "org.themoviedb.api.api_key"})
@RequiredArgsConstructor
public class TmdbMovieDetailsService implements MovieDetailsService {

    private final RestTemplate rest = new RestTemplate();
    private final TmdbServiceProps props;

    @Override
    public Optional<MovieDetails> getMovieById(long id) {
        ResponseEntity<TmdbMovie> entity =
                rest.getForEntity(movieByIdUri(id), TmdbMovie.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(entity.getBody());
        }
        return Optional.empty();
    }

    @Override
    public List<MovieDetails> findMovies(String query) {
        try {
            ResponseEntity<TmdbMovieList> entity =
                    rest.getForEntity(moviesByQueryUri(query), TmdbMovieList.class);

            if (entity.getStatusCode().is2xxSuccessful()) {
                List<TmdbMovie> results = Objects.requireNonNull(entity.getBody()).getResults();
                return results.stream()
                        .map(d -> (MovieDetails) d)
                        .collect(Collectors.toUnmodifiableList());
            }
        } catch (URISyntaxException e) {
            log.error("Query URI creation failed for '" + query + "'", e);
        }

        return List.of();
    }

    @Override
    public Optional<MovieDetails> getMovieByTitle(String title) {
        return findMovies(title).stream()
                .filter(m -> m.getTitle().equals(title))
                .max(Comparator.comparing(MovieDetails::getReleaseDate));
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

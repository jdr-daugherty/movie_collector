package com.daugherty.movie.collector;

import com.daugherty.movie.collector.details.themoviedb.TmdbMovieDetailsService;
import com.daugherty.movie.collector.details.themoviedb.TmdbServiceProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TmdbMovieDetailsService.class, TmdbServiceProps.class})
public class CollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectorApplication.class, args);
	}

}

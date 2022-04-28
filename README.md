# Movie Collector Test Project

An extremely simple back-end API for a collection of movies and reviews.
Movie details (tag line, release date, etc.) are pulled from The MovieDB's API dynamically.
Refer to https://www.themoviedb.org/documentation/api for more information.

This project serves as an attempt to learn and record the Spring Boot _Best Practices_ I was able to find in the first few months of training.

## TODO List
- Consider WireMock for testing TMDB interface.
- Search (/search?query=) to find movies in TMDB
- Oauth2 authentication via Google Cloud
- Users with reviews and movies stored by user.
- Consider replacing PUT and POST handlers (/movies and /reviews) to use @RequestParam for inputs instead of DTOs
  - Use @RequestBody for review body

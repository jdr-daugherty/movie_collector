# Movie Collector Test Project

Spring and TDD practice.

## TODO List
- Split controller end-point tests into a separate class and fill out the gaps that creates.
- Search (/search?query=) to find movies in TMDB
- Oauth2 authentication via Google Cloud
- Users with reviews and movies stored by user.
- Consider WireMock for testing TMDB interface.
- Consider replacing PUT and POST handlers (/movies and /reviews) to use @RequestParam for inputs instead of DTOs
  - Use @RequestBody for review body

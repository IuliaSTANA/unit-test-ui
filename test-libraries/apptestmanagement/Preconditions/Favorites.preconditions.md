# Favorites Preconditions

This document describes the reusable state blocks required for testing the Favorites screen.

---

@Preconditions_Favorites_EmptyState
- The user has no saved favorite locations in local storage or repository.
- The favorites list is empty.
💡Tip: Override `FavoritesRepository.favorites` with `flowOf(emptyList())` or clear local favorites database table.

---

@Preconditions_Favorites_WithFavorites
- The user has saved favorite locations (e.g., Berlin, London, Tokyo) in local storage.
- The `FavoritesRepository.favorites` stream emits a non-empty list of `FavoriteLocation` objects.
💡Tip: Inject `FavoriteLocation` entities via `FavoritesRepository.addFavorite()` or mock `FavoritesRepository.favorites` emitting `# Data: Data/Favorites/favorite-locations.json`.

---

@Preconditions_Favorites_FetchingWeather
- At least one favorite location exists in the user's list.
- The weather API request `GET /v1/forecast?latitude={lat}&longitude={lon}` for that location is pending or delayed.
💡Tip: Mock `WeatherRepository.getWeather()` to suspend or delay response execution to simulate an in-flight loading state.

---

@Preconditions_Favorites_WeatherError
- At least one favorite location exists in the user's list.
- The weather API request `GET /v1/forecast?latitude={lat}&longitude={lon}` returned a failure or HTTP error status.
💡Tip: Mock `WeatherRepository.getWeather()` to return `Result.failure(Exception("Failed to fetch weather"))` or return error response `# Data: Data/Favorites/weather-error-response.json`.

---

@Preconditions_Favorites_FetchingLocation
- The user taps "Add Current Location".
- The `LocationTracker.getCurrentLocation()` request is currently resolving device GPS coordinates.
💡Tip: Set `isLoadingLocation` state to `true` in `FavoritesViewModel` or delay `LocationTracker.getCurrentLocation()` response.

---

@Preconditions_Favorites_NoLocationPermission
- The app has not been granted `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION` permissions by the user.
- `LocationTracker.hasLocationPermission()` returns `false`.
💡Tip: Mock `LocationTracker.hasLocationPermission()` to return `false` or revoke location permissions on the test device/emulator.

# Core Flows Preconditions

This document describes the reusable state blocks required for testing core app flows and navigation.

---

@Preconditions_CoreFlows_DefaultState
- The application is launched with default test dependencies (`TestRepositoryModule`).
- `FakeFavoritesRepository` and `FakeWeatherRepository` provide initial mock data for Droidcon locations (e.g. Berlin, London, Tokyo, SF).
💡Tip: Ensure `HiltAndroidRule.inject()` is executed before test scenarios run.

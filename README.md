# todo-kmp

A Kotlin Multiplatform todo app. Android UI with Jetpack Compose; shared business
logic and data layer in Kotlin (`commonMain`). iOS UI (SwiftUI) to be added later.

It is a native rewrite of the Flutter app `todo-app`, talking to the same backend
(`todo-be`, a FastAPI server).

## Modules
- `shared/` — KMP shared module: model, domain, data, HTTP client (Ktor).
- `androidApp/` — Android application (Jetpack Compose + ViewModel).

## Requirements
- Android Studio (with Android SDK) / JDK 21
- The backend (`todo-be`) running for the networked features (see `todo-be/README.md`).

## Run
Open in Android Studio and run the `androidApp` configuration, or:

```
./gradlew :androidApp:assembleDebug
./gradlew :shared:testDebugUnitTest
```

The app expects the backend at `http://10.0.2.2:8000` (the Android emulator's alias
for the host's `localhost:8000`).

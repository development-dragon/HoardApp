# Hoard

An Android task/reward points app. Create your own tasks and rewards on the fly, earn points by completing tasks, and redeem points for rewards. Your profile shows your available point balance and redemption history, and any redeemed reward can be removed (refunding its points).

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Room (local SQLite persistence — all data stays on-device)
- Navigation Compose (Tasks / Rewards / Profile bottom navigation)
- minSdk 26, targetSdk/compileSdk 34

## How it works

- **Tasks tab** — add tasks with a name and point value. Tap the check icon to complete a task and add its points to your balance (the task stays so you can reuse it). Tap the trash icon to delete a task type entirely.
- **Rewards tab** — add rewards with a name and point cost. Tap the redeem icon to spend points on a reward; if you don't have enough points you'll see a message and nothing is deducted. Tap the trash icon to delete a reward type.
- **Profile tab** — shows your total available points and your redemption history. Tap the trash icon on a redeemed reward to remove it, which refunds its points back to your balance.

## Building the app

This project was scaffolded in a sandboxed environment without access to the Android SDK or Google's Maven repository, so the build could not be verified end-to-end here. To build it:

1. Open the project folder in Android Studio (Koala/2024.1 or newer recommended), or run from the command line:
   ```
   ./gradlew assembleDebug
   ```
2. Android Studio will prompt to install any missing SDK platforms/build tools (compileSdk 34) automatically.
3. Run on a device/emulator running Android 8.0 (API 26) or newer.

If you hit a dependency version mismatch, the versions used are: AGP 8.5.2, Kotlin 2.0.20, Compose BOM 2024.09.00, Room 2.6.1, Navigation Compose 2.8.0 — all can be bumped in `build.gradle.kts` / `app/build.gradle.kts` if Android Studio suggests newer stable releases.

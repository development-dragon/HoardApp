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

## Getting the APK on your phone (no computer needed)

Every push to `claude/android-task-reward-app-ff86pp` runs `.github/workflows/android-build.yml`, which builds a debug APK on GitHub's servers and publishes it to a release tag called **`debug-latest`**:

1. From your phone, open `https://github.com/development-dragon/HoardApp/releases/tag/debug-latest`.
2. Download the `.apk` asset directly (no zip to extract).
3. Open the downloaded file to install it — Android will prompt you to allow "install unknown apps" for your browser or Files app the first time.

You can also trigger a build manually anytime from the **Actions** tab (works fine from the GitHub mobile app or mobile browser) using the "Run workflow" button on the "Build debug APK" workflow — useful if you want a fresh build without pushing a new commit. If the release step ever fails (e.g. Actions lacks write permission on the repo), the same APK is still available as a workflow run artifact under the Actions tab, just zipped.

## Building the app locally (optional)

This project was scaffolded in a sandboxed environment without access to the Android SDK or Google's Maven repository, so the build could not be verified end-to-end there — CI (above) is the verified build path. To build locally instead:

1. Open the project folder in Android Studio (Koala/2024.1 or newer recommended), or run from the command line:
   ```
   ./gradlew assembleDebug
   ```
2. Android Studio will prompt to install any missing SDK platforms/build tools (compileSdk 34) automatically.
3. Run on a device/emulator running Android 8.0 (API 26) or newer.

If you hit a dependency version mismatch, the versions used are: AGP 8.5.2, Kotlin 2.0.20, Compose BOM 2024.09.00, Room 2.6.1, Navigation Compose 2.8.0 — all can be bumped in `build.gradle.kts` / `app/build.gradle.kts` if Android Studio suggests newer stable releases.

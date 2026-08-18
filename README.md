# Hoard

An Android task/reward points app. Create your own tasks and rewards on the fly, earn points by completing tasks, and redeem points for rewards. Your profile shows your available point balance and redemption history, and lets you clear a redeemed reward off the list once you've actually claimed it.

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Room (local SQLite persistence — all data stays on-device)
- Navigation Compose (Tasks / Rewards / Profile bottom navigation)
- minSdk 26, targetSdk/compileSdk 34

## How it works

- **Tasks tab** — add tasks with a name and point value. Tap the pencil icon to edit a task's name/points, the check icon to complete it and add its points to your balance (the task stays so you can reuse it), or the trash icon to delete it entirely.
- **Rewards tab** — add rewards with a name and point cost. Tap the pencil icon to edit a reward, the redeem icon to spend points on it (if you don't have enough points you'll see a message and nothing is deducted), or the trash icon to delete the reward type.
- **Profile tab** — shows your total available points and your redemption history. Points are spent as soon as you redeem a reward on the Rewards tab. Once you've actually claimed a redeemed reward in real life, tap its check icon here to clear it from the list — its points stay spent; this doesn't refund them.

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

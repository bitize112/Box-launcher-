# Contributing to BoxLauncher

Thanks for your interest in contributing.

## Development workflow
1. Fork and create a feature branch.
2. Keep changes focused and include tests/checks when possible.
3. Run local checks before opening a PR.
4. Open a PR with a clear summary and motivation.

## Commit style
- Use imperative commit messages, e.g. `Add NDK JNI hello-world bridge`.
- Prefer small, reviewable commits.

## Code areas
- `core/` — native C++ hook/runtime layer.
- `android/` — Android app and JNI integration.
- `js-api/` — JavaScript-facing mod API and backend selection.
- `docs/` — architecture and modding docs.

## Reporting issues
Please include:
- device + Android version,
- app version/commit hash,
- repro steps,
- logs (`adb logcat` snippets if relevant).

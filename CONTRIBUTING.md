# Contributing to BoxLauncher

Thanks for your interest in contributing.

## Getting started

1. Fork the repository and create a feature branch from `main`.
2. Keep changes scoped to a single concern (cleanup, Android build, native core, etc.).
3. Run the relevant checks before opening a pull request.

## Development structure

- `android/` — Android app project.
- `core/` — native C++ engine and hooking backends.
- `js-api/` — JavaScript API surface for mods.
- `docs/` — architecture and design documentation.

## Pull requests

- Use clear commit messages.
- Describe what changed and why.
- Include test/build commands and output in the PR description.
- If UI changes are made, attach screenshots.

## Code style

- Prefer small, reviewable patches.
- Keep naming consistent with existing modules.
- Update docs when behavior or structure changes.

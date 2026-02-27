# BoxLauncher

JavaScript-first Bedrock launcher prototype with pluggable native hook backends.

## Status
This update focuses on **Phase 1 + Phase 2** foundations:
- repository hygiene files (`LICENSE`, `CONTRIBUTING.md`, `.gitignore`)
- cleaned project layout (`core/`, `android/`, `js-api/`, `docs/`)
- Android NDK wiring + JNI hello-world path

## Project layout
```
.
├── core/          # C++ native layer (libboxlauncher.so)
├── android/       # Android app project
├── js-api/        # JavaScript API/backend selector
├── docs/          # architecture docs
├── LICENSE
├── CONTRIBUTING.md
└── README.md
```

## Hook backend support (prototype)
- Frida-Gum
- And64InlineHook
- ShadowHook

Auto-selection logic lives in `js-api/backendSelector.js`.

## Android native proof-of-life
The Android app now:
1. Builds against an external CMake project at `core/CMakeLists.txt`
2. Compiles `libboxlauncher.so` for `arm64-v8a`
3. Calls `stringFromNative()` JNI and displays “Hello from native libboxlauncher.so” in UI

## Build commands
```bash
# JS selector smoke test
node -e "const {chooseHookEngine}=require('./js-api/backendSelector'); console.log(chooseHookEngine({workload:'performanceCritical'}).engine.id)"


# Android debug APK build
cd android
JAVA_HOME=/path/to/jdk17 gradle assembleDebug
```

Expected APK output:
- `android/app/build/outputs/apk/debug/app-debug.apk`

## Note on repository rename
Renaming the actual git repository from `Box-launcher-` to `BoxLauncher` must be done on the remote hosting service (GitHub/GitLab) settings page.

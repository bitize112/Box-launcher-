# Box Launcher

A JavaScript-first mod launcher concept for **Minecraft Bedrock Edition** with pluggable hooking backends:

- **Frida-Gum** for complex/dynamic instrumentation
- **And64InlineHook** for lightweight, performance-critical hooks
- **ShadowHook** for production-grade stability

If a mod developer does not choose a backend, the launcher can **auto-select** the best backend based on workload profile.

## Yes — now includes Java UI and Android APK project
This repository now has both:

- Desktop Java Swing prototype: `java/src/main/java/com/boxlauncher/ui/LauncherConfiguratorFrame.java`
- Android APK prototype project: `android/`

The Android app lets you:
- choose workload profile
- optionally force backend (Frida-Gum / And64InlineHook / ShadowHook)
- use automatic selection when override is not selected

## Current prototype layout
- `src/backendSelector.js`: JavaScript backend scoring + selection logic
- `java/src/main/java/com/boxlauncher/ui/*`: Java Swing desktop UI/UX prototype
- `android/`: Android app module for APK testing
- `docs/launcher-architecture.md`: architecture and modding model (JS-first + native extensions)

## Backend selection example (JavaScript)

```js
const { chooseHookEngine } = require('./src/backendSelector');

const auto = chooseHookEngine({ workload: 'productionSafe' });
console.log(auto.engine.id); // shadowhook (usually)

const forced = chooseHookEngine({ preferredEngine: 'frida-gum' });
console.log(forced.engine.label); // Frida-Gum
```

## Run Java desktop prototype

```bash
javac java/src/main/java/com/boxlauncher/ui/*.java
java -cp java/src/main/java com.boxlauncher.ui.LauncherConfiguratorFrame
```

## Build Android debug APK

```bash
cd android
JAVA_HOME=/path/to/jdk17 gradle assembleDebug
```

Expected APK path:
- `android/app/build/outputs/apk/debug/app-debug.apk`

## Supported workload profiles
- `balanced` (default)
- `heavyInstrumentation`
- `performanceCritical`
- `productionSafe`

## JavaScript-first, multi-language capable
Mods are expected to be primarily JavaScript, while optionally importing native logic in:

- C/C++ shared libraries (`.so`)
- Rust (`cdylib`) libraries
- WebAssembly modules (`.wasm`)

See: `docs/launcher-architecture.md`.

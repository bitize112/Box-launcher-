# BoxLauncher

A JavaScript-first mod launcher concept for **Minecraft Bedrock Edition** with pluggable hooking backends:

- **Frida-Gum** for complex/dynamic instrumentation
- **And64InlineHook** for lightweight, performance-critical hooks
- **ShadowHook** for production-grade stability

If a mod developer does not choose a backend, the launcher can **auto-select** the best backend based on workload profile.

## Repository layout

```text
BoxLauncher/
├── core/          # C++ native hooking engine
├── android/       # Android APK project
├── js-api/        # JavaScript mod API
├── docs/          # architecture docs
└── README.md
```

Additional legacy prototype assets are kept under `docs/legacy/`.

## Current prototypes

- Android APK prototype: `android/`
- JavaScript backend scoring logic: `js-api/backendSelector.js`
- Architecture docs: `docs/launcher-architecture.md`

Legacy desktop Swing prototype source is now in:
- `docs/legacy/java-swing/src/main/java/com/boxlauncher/ui/`

## Backend selection example (JavaScript)

```js
const { chooseHookEngine } = require('./js-api/backendSelector');

const auto = chooseHookEngine({ workload: 'productionSafe' });
console.log(auto.engine.id); // shadowhook (usually)

const forced = chooseHookEngine({ preferredEngine: 'frida-gum' });
console.log(forced.engine.label); // Frida-Gum
```

## Build Android debug APK

```bash
cd android
JAVA_HOME=/path/to/jdk17 gradle assembleDebug
```

Expected APK path:
- `android/app/build/outputs/apk/debug/app-debug.apk`

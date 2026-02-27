# Bedrock Mod Launcher Architecture (JavaScript-first)

## Goals
- Provide a Fabric-like mod launcher workflow for **Minecraft Bedrock Edition**.
- Let mod developers write mods primarily in **JavaScript**.
- Allow optional native acceleration via **C/C++**, **Rust**, or other languages.
- Support pluggable hook backends:
  - Frida-Gum
  - And64InlineHook
  - ShadowHook
- Automatically choose the best backend when developers do not specify one.

## Backend strategy
The launcher ships a backend selector (`js-api/backendSelector.js`) that chooses an engine based on workload:

- `heavyInstrumentation` → tends toward Frida-Gum for deep, dynamic hooks.
- `performanceCritical` → tends toward And64InlineHook for low overhead.
- `productionSafe` → tends toward ShadowHook for stability.
- `balanced` (default) → weighted blend with stability emphasis.

Developers can still force a backend with `preferredEngine`.


## Java UI/UX launcher layer
- A Java Swing configurator can be used as a desktop launcher UI for mod pack authors.
- UI lets developers pick workload profile and optionally force backend.
- If backend override is not selected, the launcher auto-chooses based on workload profile.
- This keeps authoring JS-first while offering a familiar native desktop configuration experience.

## JavaScript-first mod API
Recommended mod package layout:

```text
my-mod/
  mod.json
  index.js
  native/
    libmod.so
    mod.wasm
```

Example `mod.json`:

```json
{
  "id": "com.example.fastcombat",
  "name": "Fast Combat",
  "entry": "index.js",
  "backend": "auto",
  "workload": "performanceCritical",
  "native": [
    {
      "type": "shared-library",
      "path": "native/libmod.so",
      "exports": ["onAttack", "calcDamage"]
    },
    {
      "type": "wasm",
      "path": "native/mod.wasm"
    }
  ]
}
```

## Multi-language integration options
1. **JNI/NDK shared libraries (.so)**
   - Build hooks/helpers in C/C++ or Rust (`cdylib`) and load from launcher runtime.
   - Best for direct native speed and deep integration.
2. **WASM modules**
   - Safe and portable for compute-heavy logic.
   - Easy to version and sandbox.
3. **IPC sidecar process**
   - Good for experimental languages/tools (Go, Zig, etc.) using protobuf/json-rpc.

## Runtime pipeline
1. Launcher starts and reads mod manifests.
2. For each mod, backend is selected:
   - explicit `backend` from mod config, or
   - automatic via workload profile + weighted scoring.
3. Hook backend initializes and installs required symbols/inline hooks.
4. JavaScript runtime loads mod `entry` file.
5. Native modules (if any) are mounted into JS bridge (`native.call(...)`).
6. Event bus dispatches Bedrock events to JavaScript handlers.

## Stability recommendations
- Use ShadowHook default profile for public packs and production releases.
- Restrict Frida-Gum to debug/dev profile unless needed in production.
- Expose crash-safe fallback: if preferred backend fails, retry with ShadowHook.

## Security and trust
- Sign mod manifests and verify signatures before load.
- Provide permission scopes (`memory.read`, `memory.write`, `net`, `filesystem`).
- Keep a per-mod sandbox policy and deny-by-default behavior.


## Android APK testing path
- Added an Android prototype app (`android/`) to validate launcher configuration UX on-device.
- The app mirrors backend behavior: manual override or automatic choice by workload.
- Current APK target is for UI/selection testing only; it does not yet inject hooks into the Bedrock process.

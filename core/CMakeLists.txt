cmake_minimum_required(VERSION 3.22.1)
project(boxlauncher)

add_library(
    boxlauncher
    SHARED
    src/main/cpp/boxlauncher.cpp
)

find_library(log-lib log)

# ARM64 target hint for NDK builds
if (CMAKE_ANDROID_ARCH_ABI STREQUAL "arm64-v8a")
    message(STATUS "Building boxlauncher for ARM64")
endif()

target_compile_features(boxlauncher PRIVATE cxx_std_17)

target_link_libraries(
    boxlauncher
    ${log-lib}
)

cmake_minimum_required(VERSION 3.22.1)
project(boxlauncher)

add_library(
    boxlauncher
    SHARED
    src/main.cpp
)

target_compile_features(boxlauncher PRIVATE cxx_std_17)

find_package(JNI REQUIRED)
target_link_libraries(boxlauncher PRIVATE JNI::JNI)

if(ANDROID)
    if(DEFINED CMAKE_ANDROID_ARCH_ABI AND NOT CMAKE_ANDROID_ARCH_ABI STREQUAL "arm64-v8a")
        message(FATAL_ERROR "BoxLauncher only supports arm64-v8a. Current ABI: ${CMAKE_ANDROID_ARCH_ABI}")
    endif()

    find_library(android-lib android REQUIRED)
    find_library(log-lib log REQUIRED)

    target_link_libraries(
        boxlauncher
        PRIVATE
        ${android-lib}
        ${log-lib}
    )
else()
    message(STATUS "Non-Android host build: using stub logging path for local validation")
    target_compile_definitions(boxlauncher PRIVATE BOXLAUNCHER_HOST_BUILD=1)
endif()

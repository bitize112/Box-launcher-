#include "../src/HookEngine.hpp"

#include <iostream>

namespace {

void testCallback() {}

} // namespace

int main() {
    void* original = nullptr;
    const bool installed = HookEngine::getInstance().installHook(
        "libminecraftpe.so",
        "DUMMY_PATTERN",
        "xxxxxxxxxxxxx",
        reinterpret_cast<void*>(testCallback),
        &original
    );

    HookEngine::getInstance().uninstallAll();

    if (!installed) {
        std::cerr << "installHook failed" << std::endl;
        return 1;
    }

    std::cout << "HookEngine installHook ran without crash" << std::endl;
    return 0;
}

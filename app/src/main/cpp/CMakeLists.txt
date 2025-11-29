cmake_minimum_required(VERSION 3.22.1)

project("localinter")

# Build shared library "localinter" from native-lib.cpp
add_library(
        localinter
        SHARED
        native-lib.cpp
)

# Link against Android log library
find_library(
        log-lib
        log
)

target_link_libraries(
        localinter
        ${log-lib}
)

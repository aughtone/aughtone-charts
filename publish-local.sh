#!/bin/sh
# allTests, not test: a Kotlin Multiplatform module has no plain `test` task.
# No trailing `exit 0`, so the script exits with Gradle's status and a failed build reports as failed.
./gradlew -Pskip-signing=true allTests publishToMavenLocal

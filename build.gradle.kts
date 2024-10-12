// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0-rc02" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false
    id("org.jmailen.kotlinter") version "4.2.0"
}

kotlinter {
    failBuildWhenCannotAutoFormat = true
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
}
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.wikipediatopics"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wikipediatopics"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

tasks.register<LintTask>("lintKotlin") {
    group = "formatting"
    source(files("src"))
    reports.set(
        mapOf(
            "plain" to file("build/lint-report.txt"),
            "json" to file("build/lint-report.json")
        )
    )
}

tasks.register<FormatTask>("formatKotlin") {
    group = "formatting"
    source(files("src"))
    report.set(file("build/format-report.txt"))
}

tasks.register<Copy>("copyGitHooks") {
    group = "git hooks"
    from("$rootDir/scripts/pre-commit")
    into("$rootDir/.git/hooks/")
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    /* Compose */
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    /* HTTP/Data parsing */
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

    /* Dependency Injection */
    implementation(platform("io.insert-koin:koin-bom:4.0.0"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-androidx-compose")

    /* Debug */
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /* Test */
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.2.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")

    testImplementation("androidx.test.espresso:espresso-core:3.6.1")

    testImplementation("org.hamcrest:hamcrest:2.2")

    testImplementation("org.robolectric:robolectric:4.9")

    testImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    testImplementation("androidx.compose.ui:ui-test-junit4")

    testImplementation("io.insert-koin:koin-test-junit4")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("io.mockk:mockk:1.13.13")
}
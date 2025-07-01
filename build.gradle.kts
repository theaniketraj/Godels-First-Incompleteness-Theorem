plugins {
    kotlin("jvm") version "1.8.0"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin standard library is included by default
}

tasks.test {
    useJUnitPlatform()
}

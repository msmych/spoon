plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.javalin)
    implementation(libs.themoviedbapi)
    implementation(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
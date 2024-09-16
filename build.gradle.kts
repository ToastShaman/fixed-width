plugins {
    java
    `java-library`
    `maven-publish`
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.github.toastshaman.fixed-width"
version = project.findProperty("version") ?: "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("io.vavr:vavr:0.10.4")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    java {
        palantirJavaFormat()
    }
}

tasks.withType<JavaCompile> {
    finalizedBy(tasks.spotlessApply)
}
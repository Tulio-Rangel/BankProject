plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("jacoco")
    id("org.sonarqube") version "4.4.1.3373"
}

jacoco {
    toolVersion = "0.8.9"
}

group = "com.tulio"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")  // Remove explicit version
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("dockerBuild") {
    group = "docker"
    description = "Build the Docker image"
    doLast {
        val version = project.version.toString()
        exec {
            commandLine("docker", "build", "--build-arg", "APP_VERSION=$version", "-t", "bank-sofka:$version", ".")
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("${buildDir}/reports/jacoco/test/jacocoTestReport.xml"))
        html.outputLocation.set(file("${buildDir}/reports/jacoco/test/html"))
    }
    dependsOn(tasks.test)
}



sonar {
    properties {
        property("sonar.projectKey", "Tulio-Rangel_BankProject")
        property("sonar.organization", "tulio-rangel")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.binaries", setOf("${buildDir}/classes/java/main"))
        property("sonar.java.test.binaries", setOf("${buildDir}/classes/java/test"))
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.sources", setOf("src/main/java"))
        property("sonar.tests", setOf("src/test/java"))
        property("sonar.gradle.skipCompile", true)
        property("sonar.login", System.getenv("SONAR_TOKEN"))
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.java.source", "17")
        property("sonar.scm.provider", "git")
        property("sonar.coverage.exclusions", "**test**")
    }
}

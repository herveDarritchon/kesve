import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    jacoco
    `java-library`
    `maven-publish`
}

group = "fr.hervedarritchon.utils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    testCompile("io.kotlintest:kotlintest-runner-console:3.3.2")
    testCompile("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testCompile("org.slf4j:slf4j-simple:1.7.26")
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

jacoco {
    toolVersion = "0.8.3"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "kesve-library"
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Kesve Library")
                description.set("A library to handle CSV file developed in Kotlin")
                url.set("https://github.com/herveDarritchon/kesve")
                properties.set(mapOf(
                    "myProp" to "value",
                    "prop.with.dots" to "anotherValue"
                ))
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("herveDarritchon")
                        name.set("Hervé Darritchon")
                        email.set("herve.darritchon@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/herveDarritchon/kesve.git")
                    developerConnection.set("scm:git:git@github.com:herveDarritchon/kesve.git")
                    url.set("https://github.com/herveDarritchon/kesve")
                }
            }
        }
    }
    repositories {
        maven {
            // change URLs to point to your repos, e.g. http://my.org/repo
            val releasesRepoUrl = uri("$buildDir/repos/releases")
            val snapshotsRepoUrl = uri("$buildDir/repos/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
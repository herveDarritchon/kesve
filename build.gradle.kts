import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    jacoco
    `java-library`
    `maven-publish`
    id("com.gradle.build-scan") version "2.3"
    id("com.jfrog.bintray") version "1.8.4"
    id("com.jfrog.artifactory") version "4.9.7"
}

val snapshotEnvValue = System.getenv("snapshot")?.toBoolean()

val snapshotExtension: String = when {
    snapshotEnvValue ?: true -> "-SNAPSHOT"
    else -> ""
}
group = "fr.hervedarritchon.utils.kesve"
version = "0.0.1$snapshotExtension"

repositories {
    mavenCentral()
    jcenter()
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
        create<MavenPublication>("mavenPublication") {
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
                properties.set(
                    mapOf(
                        "myProp" to "value",
                        "prop.with.dots" to "anotherValue"
                    )
                )
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

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}

//bintray {
//    user = "bintray_user"
//    key = "bintray_api_key"
//    with(pkg) {
//        repo = "kotlin"
//        name = "kesve"
//        setLicenses("Apache-2.0")
//        vcsUrl = "https://github.com/herveDarritchon/kesve"
//        with(version) {
//            name = "0.0.1-Final"
//            desc = "Kesve Library 0.0.1 final"
//            released = LocalDate().toString()
//            vcsTag = "0.0.1"
//            attributes = mapOf("gradle-plugin" to "com.use.less:com.use.less.gradle:gradle-useless-plugin")
//        }
//    }
//}

apply {
    from("publish.gradle")
}
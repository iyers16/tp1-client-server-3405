plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

// Task to build the Client JAR
tasks.register<Jar>("clientJar") {
    archiveFileName.set("client.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output) // Includes all compiled classes
    manifest {
        attributes["Main-Class"] = "client.Client" // Ensure this matches your main client class
    }
}

// Task to build the Server JAR
tasks.register<Jar>("serverJar") {
    archiveFileName.set("server.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output) // Includes all compiled classes
    manifest {
        attributes["Main-Class"] = "server.Server" // Ensure this matches your main server class
    }
}

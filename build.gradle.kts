plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application.mainClass.set("org.glow.discordBot.Bot")
group = "org.glow"
version = "1.0-SNAPSHOT"

val jdaVersion = "5.0.0-beta.13"

repositories {
    mavenCentral()
}

dependencies {
    // logback
    implementation("ch.qos.logback:logback-classic:1.2.9")
    // jackson
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    // JDA
    implementation("net.dv8tion:JDA:$jdaVersion")
    // testing
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    sourceCompatibility = "18"
}

tasks.test {
    useJUnitPlatform()
}
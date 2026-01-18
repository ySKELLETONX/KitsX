plugins {
    id("java")
}

group = "me.skelletonx.br"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies { //a
    compileOnly(files("libs/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
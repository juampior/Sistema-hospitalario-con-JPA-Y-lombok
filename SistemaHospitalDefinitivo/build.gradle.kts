plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly ("org.projectlombok:lombok:1.18.38")
    annotationProcessor ("org.projectlombok:lombok:1.18.38")

    // JPA dependencies
    implementation ("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation ("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation ("org.slf4j:slf4j-simple:2.0.13")

    // H2 Database
    implementation ("com.h2database:h2:2.2.224")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")

    // Logger (opcional pero recomendable)
    implementation ("org.slf4j:slf4j-simple:2.0.13")


}

tasks.test {
    useJUnitPlatform()
}
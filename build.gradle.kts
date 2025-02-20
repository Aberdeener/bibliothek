import net.kyori.indra.repository.sonatypeSnapshots

plugins {
  id("java")

  alias(libs.plugins.indra)
  alias(libs.plugins.indra.checkstyle)
  alias(libs.plugins.indra.license.header)
  alias(libs.plugins.spring.dependency.management)
  alias(libs.plugins.spring.boot)
}

group = "io.papermc"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  sonatypeSnapshots()
}

indra {
  javaVersions {
    target(17)
  }

  github("PaperMC", "bibliothek")
  mitLicense()
}

dependencies {
  annotationProcessor(libs.spring.boot.configuration.processor)
  checkstyle(libs.stylecheck)
  implementation(libs.semver4j)
  implementation(libs.coffee)
  implementation(libs.spring.doc)
  implementation(libs.spring.boot.starter.data.mongodb)
  implementation(libs.spring.boot.starter.undertow)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.web) {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat") // we use undertow
  }
  implementation(libs.spring.boot.starter.test) {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
}

tasks {
  bootJar {
    launchScript()
  }

  // From StackOverflow: https://stackoverflow.com/a/53087407
  // Licensed under: CC BY-SA 4.0
  // Adapted to Kotlin
  register<Copy>("buildForDocker") {
    from(bootJar)
    into("build/libs/docker")
    rename { fileName ->
      // a simple way is to remove the "-$version" from the jar filename
      // but you can customize the filename replacement rule as you wish.
      fileName.replace("-$version", "")
    }
  }
}

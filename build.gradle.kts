plugins {
  id("fabric-loom") version "0.4.28"
  id("signing")
}

group = "dev.sapphic"
version = "7.1.0"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

minecraft {
  refmapName = "mixins/fovlock/refmap.json"
  runDir = "run"
}

dependencies {
  minecraft("com.mojang:minecraft:1.16")
  mappings("net.fabricmc:yarn:1.16+build.1:v2")
  modImplementation("net.fabricmc:fabric-loader:0.8.8+build.202")

  implementation("org.jetbrains:annotations:19.0.0")
  implementation("org.checkerframework:checker-qual:3.4.0")
  implementation("com.google.code.findbugs:jsr305:3.0.2")

  modImplementation(include(fabricApi.module(
    "fabric-resource-loader-v0", "0.13.1+build.370-1.16"
  ))!!)
}

tasks.withType<ProcessResources> {
  filesMatching("/fabric.mod.json") {
    expand("version" to version)
  }
}

tasks.withType<JavaCompile> {
  with(options) {
    isFork = true
    isDeprecation = true
    encoding = "UTF-8"
    compilerArgs.addAll(listOf(
      "-Xlint:all", "-parameters"
    ))
  }
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}

plugins {
  java
  id("fabric-loom") version "0.2.7-SNAPSHOT"
}

group = "dev.sapphic"
version = "6.0.0"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

minecraft {
  refmapName = "mixins/fovlock/refmap.json"
  runDir = "run"
}

dependencies {
  minecraft("com.mojang:minecraft:1.15.2")
  mappings("net.fabricmc:yarn:1.15.2+build.15:v2")
  modImplementation("net.fabricmc:fabric-loader:0.8.2+build.194")
  fabricApi(name = "resource-loader-v0", version = "0.1.14+2fd224cae8")
  implementation("org.jetbrains:annotations:19.0.0")
  implementation("org.checkerframework:checker-qual:3.3.0")
}

tasks.withType<ProcessResources> {
  filesMatching("fabric.mod.json") {
    expand("version" to version)
  }
}

tasks.withType<JavaCompile> {
  options.run {
    isFork = true
    isVerbose = true
    encoding = "UTF-8"
    compilerArgs.addAll(listOf(
      "-Xlint:all",
      "-XprintProcessorInfo",
      "-XprintRounds"
    ))
  }
}

fun DependencyHandlerScope.fabricApi(name: String, version: String) {
  modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-$name", version = version)
  include(group = "net.fabricmc.fabric-api", name = "fabric-$name", version = version)
}

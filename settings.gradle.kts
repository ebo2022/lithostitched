rootProject.name = "lithostitched"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.msrandom.net/repository/cloche/")
    }
}

// TODO: remove upon modrinth release
includeBuild("FlowSched") {
    dependencySubstitution {
        substitute(module("com.ishland.flowsched:flowsched"))
            .using(project(":"))
    }
}
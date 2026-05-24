plugins {
    kotlin("jvm") version "2.1.0"
    id("earth.terrarium.cloche") version "0.18.14"
}

repositories {
    cloche.librariesMinecraft()
    mavenCentral()
    mavenLocal()
    cloche {
        main()
        mavenNeoforgedMeta()
        mavenNeoforged()
        mavenFabric()
    }
    // TODO: remove upon modrinth release
    maven {
        url = uri("https://maven.bawnorton.com/releases")
        content {
            includeGroup("com.github.bawnorton.mixinsquared")
        }
    }
    maven {
        name = "CaffeineMC snapshots"
        url = uri("https://maven.caffeinemc.net/snapshots")
    }
    maven {
        name = "CaffeineMC releases"
        url = uri("https://maven.caffeinemc.net/releases")
    }
}

group = "dev.worldgen.lithostitched"
version = "1.7.5"

cloche {
    metadata {
        modId = "lithostitched"
        name = "Lithostitched"
        description = "A library mod with new configurability and compatibility enhancements for worldgen"
        license = "MIT"
        icon = "pack.png"

        author("Apollo")
    }

    common {
        mixins.from(file("src/common/main/lithostitched.mixins.json"))

        dependencies {
            compileOnly("org.spongepowered:mixin:0.8.3")
        }
    }

    val sharedOld = common("shared:21.1") {
        mixins.from(file("src/shared/21.1/main/lithostitched.21.1.mixins.json"))
        accessWideners.from(file("src/shared/21.1/main/lithostitched.21.1.accesswidener"))
    }
    val sharedNew = common("shared:26.1") {
        mixins.from(file("src/shared/26.1/main/lithostitched.26.1.mixins.json"))
        accessWideners.from(file("src/shared/26.1/main/lithostitched.26.1.accesswidener"))
    }

    fabric("fabric:21.1") {
        dependsOn(sharedOld)

        loaderVersion = "0.18.4"
        minecraftVersion = "1.21.1"
        mixins.from(file("src/fabric/21.1/main/lithostitched.fabric.mixins.json"))

        mappings {
            official()
            custom(minecraftVersion.map {
                project.dependencies.create(files("mappings/$it.tiny"))
            })
        }

        dependencies {
            fabricApi("0.116.1")
            api("com.ishland.flowsched:flowsched")
        }

        includedClient()
        runs {
            client()
            server()
        }

        metadata {
            entrypoint("main") {
                value = "dev.worldgen.lithostitched.LithostitchedFabric"
            }
        }
    }

    fabric("fabric:26.1") {
        dependsOn(sharedNew)

        loaderVersion = "0.19.2"
        minecraftVersion = "26.1.2"
        mixins.from(file("src/fabric/26.1/main/lithostitched.fabric.mixins.json"))

        dependencies {
            fabricApi("0.148.0")
            modImplementation("com.ishland.c2me:c2me:0.3.7+alpha.0.69-dirty")
        }

        includedClient()
        runs {
            client()
            server()
        }

        metadata {
            entrypoint("client") {
                value = "dev.worldgen.lithostitched.client.LithostitchedFabricClient"
            }
            entrypoint("main") {
                value = "dev.worldgen.lithostitched.LithostitchedFabric"
            }
        }
    }

    neoforge("neoforge:21.1") {
        dependsOn(sharedOld)

        loaderVersion = "21.1.222"
        minecraftVersion = "1.21.1"
        mixins.from(file("src/neoforge/21.1/main/lithostitched.neoforge.mixins.json"))

        mappings {
            official()
            custom(minecraftVersion.map {
                project.dependencies.create(files("mappings/$it.tiny"))
            })
        }

        runs {
            client()
            server()
        }
    }

    neoforge("neoforge:26.1") {
        dependsOn(sharedNew)

        loaderVersion = "26.1.2.42-beta"
        minecraftVersion = "26.1.2"
        mixins.from(file("src/neoforge/26.1/main/lithostitched.neoforge.mixins.json"))

        runs {
            client()
            server()
        }
    }
}
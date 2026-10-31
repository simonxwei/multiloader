<img src="common/src/main/resources/icon.png" width="128" alt="mod icon">

# multiloader

A Gradle development template for one Minecraft mod targeting **Fabric** and **NeoForge**, with shared loader-independent code.

---

## Quick Start

| Step | Action                                                                                                                                          |
|------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | Install the JDK specified by `java_version` and import the Gradle project.                                                                      |
| 2    | Set the project identity in `settings.gradle` and `gradle.properties`; rename Java packages, types, method prefixes, and resources accordingly. |
| 3    | Replace template path placeholders with literal references, including Mixin configurations and loader metadata.                                 |
| 4    | [Build and test both loaders](DEVELOPMENT.md#build-and-test), including dedicated servers.                                                      |

**Scope:** Initialization changes project identity and references only.
Preserve examples, artwork, build configuration, and project structure unless explicitly required or requested otherwise.

Follow the [setup checklist](DEVELOPMENT.md#create-a-mod); changing properties alone does not rename files or Java types.

## Projects

| Project       | Responsibility                                                 |
|---------------|----------------------------------------------------------------|
| `common`      | Shared main and separate client sources                        |
| `fabric`      | Fabric integration and data generation                         |
| `neoforge`    | NeoForge integration, configuration hooks, and data generation |
| `build-logic` | Shared Gradle conventions                                      |

See [Development](DEVELOPMENT.md) for setup, shared definitions, code conventions, and Git workflow.
See [Releasing](RELEASING.md) for versions, artifacts, and publication.

## Upstream

Based on [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) and maintained independently.
This is not an official Fabric, NeoForge, or upstream template project.

## License

The template uses [CC0 1.0 Universal](LICENSE).
Derived mods may choose another license; update metadata and documentation accordingly.

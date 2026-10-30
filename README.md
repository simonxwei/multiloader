<img src="common/src/main/resources/icon.png" width="128" alt="mod icon">

# multiloader

A Gradle development template for one Minecraft mod targeting **Fabric** and **NeoForge**, with shared loader-independent code.

---

## Quick Start

1. Install the JDK specified by `java_version` and import the Gradle project.
2. Set the project identity and dependencies in `settings.gradle` and `gradle.properties`; refactor Java names and rename resources together.
3. Resolve template path placeholders to literal references in the new mod; replace artwork, example data, and documentation.
4. Use the included wrapper to [build and test both loaders](DEVELOPMENT.md#build-and-test), including dedicated servers.

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

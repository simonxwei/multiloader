<img src="common/src/main/resources/icon.png" width="128" alt="mod icon">

# multiloader

A Gradle template for one Minecraft mod on **Fabric** and **NeoForge**, with shared loader-independent code.

---

## Quick Start

| Step | Action                                                                                                                  |
|------|-------------------------------------------------------------------------------------------------------------------------|
| 1    | Install the JDK specified by `java_version` and import the Gradle project.                                              |
| 2    | Match the repository, root project name, and README title; configure Mod ID and display name independently.             |
| 3    | Rename Java packages, types, method prefixes, and resource identifiers; fix Mixin, entrypoint, and class-tweaker paths. |
| 4    | [Build and test both loaders](DEVELOPMENT.md#build-and-test), including dedicated servers.                              |

**Scope:** Change identities and their references only.
Preserve artwork, examples, generated data, build configuration, and structure unless explicitly requested or required.

See the [initialization checklist](DEVELOPMENT.md#create-a-mod) for exact naming and path rules.

## Projects

| Module        | Responsibility                                                 |
|---------------|----------------------------------------------------------------|
| `common`      | Shared main and separate client sources                        |
| `fabric`      | Fabric integration and data generation                         |
| `neoforge`    | NeoForge integration, configuration hooks, and data generation |
| `build-logic` | Shared Gradle conventions                                      |

See [Development](DEVELOPMENT.md) for setup, architecture, conventions, and testing.

See [Releasing](RELEASING.md) for versioning and distribution.

## Upstream

Based on [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) and maintained independently.
This is not an official Fabric, NeoForge, or upstream template project.

## License

The template uses [CC0 1.0 Universal](LICENSE).
Derived mods may choose another license; update metadata and documentation accordingly.

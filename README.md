<img src="common/src/main/resources/banner.png" width="128" alt="mod icon">

# multiloader

A Gradle template for a single Minecraft mod targeting **Fabric** and **NeoForge**, with shared loader-independent code.

> This is a development template, not a ready-to-install mod.
> Replace the example identity, code, resources, and documentation when creating a project.

---

## Quick Start

1. Rename `rootProject.name` in `settings.gradle`; configure the Minecraft target, dependencies, and `mod_*` properties in `gradle.properties`.
2. Rename the Java package, `Template*` classes, entrypoints, and referenced metadata files.
3. Replace example code, generated resources, artwork, and this README.
4. Refresh Gradle; build and test both loaders, including dedicated servers.

Follow the complete [setup checklist](DEVELOPMENT.md#create-a-mod) before using the template for a new mod.

## Projects

| Project       | Contents                                                 |
|---------------|----------------------------------------------------------|
| `common`      | Shared code and separate client-only sources             |
| `fabric`      | Fabric entrypoints, integrations, and data generation    |
| `neoforge`    | NeoForge integration, configuration, and data generation |
| `build-logic` | Shared Gradle conventions                                |

## Guides

- **[Development](DEVELOPMENT.md):** setup, everyday Git workflow, tests, branch synchronization, and code conventions.
- **[Releasing](RELEASING.md):** version numbers, tags, JARs, verification, and GitHub Releases.

## Upstream

Based on [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) and maintained independently.
Not an official Fabric, NeoForge, or upstream MultiLoader project.

## License

This template uses [CC0 1.0 Universal](LICENSE).
Mods created from it may use a different license; update the mod metadata and documentation accordingly.

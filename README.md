<img src="common/src/main/resources/banner.png" width="128" alt="Multiloader template image">

# multiloader

A Gradle template for developing a single Minecraft mod for **Fabric** and **NeoForge**, with shared, loader-independent code.

This repository is a **template**, not a finished mod.
Projects created from it should replace this README with their own features, requirements, installation instructions, and support links.

---

## Structure

| Project       | Responsibility                                                                   |
|---------------|----------------------------------------------------------------------------------|
| `common`      | Shared main code and physically separate shared client code                      |
| `fabric`      | Fabric entrypoints, metadata, integrations, and data generation                  |
| `neoforge`    | NeoForge entrypoints, metadata, integrations, configuration, and data generation |
| `build-logic` | Shared Gradle conventions and source/resource wiring                             |

Shared main code does not depend on either loader, and client-only code is kept outside the shared main source set.

## Getting Started

1. Configure versions, mod metadata, and development defaults in `gradle.properties`.
2. Change the Java package and `Template*` names to suit the new mod.
3. Update mod-ID-based resource filenames and metadata references; update Java-package-based Mixin declarations and ServiceLoader descriptors separately.
4. Replace placeholder artwork and any generated example resources.
5. Build and test **both loaders**, including dedicated servers.
6. Replace this template README with the new mod's documentation.

See [DEVELOPMENT.md](DEVELOPMENT.md) for the complete setup checklist, development conventions, and verification commands.

## Documentation

- [Development](DEVELOPMENT.md) — setup, architecture, source boundaries, naming, Javadoc, exception style, and testing.
- [Releasing](RELEASING.md) — release workflows for this template repository and for mods created from it.

## Upstream

Derived from [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) and maintained independently.
This repository is not an official Fabric, NeoForge, or upstream MultiLoader project.

## License

Released under [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/). See [LICENSE](LICENSE).
Mods made from the template may choose their own licenses and must update their metadata and documentation accordingly.

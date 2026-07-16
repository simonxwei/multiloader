# multiloader

A personal Gradle template for developing one Minecraft mod on Fabric and NeoForge while keeping shared code in an independently compilable `common` project.

The template currently targets Minecraft 26.2 and Java 25. Shared versions, compatibility ranges, publication coordinates, and mod metadata are centralized in `gradle.properties`; loader-specific Gradle DSL remains in the corresponding loader project so it can still be compared with the official Fabric and NeoForge templates.

## Project Structure

| Project | Responsibility |
| --- | --- |
| `build-logic` | Shared Gradle conventions and the source/resource wiring required by the multiloader layout |
| `common` | Loader-independent Java code, generated resources, common Mixins, and shared access-change declarations |
| `fabric` | Fabric entrypoints, Fabric APIs, metadata, and the separated `main`/`client` source sets |
| `neoforge` | NeoForge entrypoints, ModDevGradle runs, metadata generation, configuration, and data generation |

The loader projects compile and package the Java sources and resources exported by `common`. Their final JARs are independently installable; players do not install the `common` JAR.

## Names and Shared Properties

The example intentionally uses different names for different layers:

- `multiloader` is the lowercase repository and Gradle root-project name.
- `template` is the lowercase example mod ID and resource namespace.
- `Template` is the example display name.
- `TemplateMod` and related PascalCase names are Java types.

The most important values in `gradle.properties` are:

| Property | Used for |
| --- | --- |
| `mod_id` | Loader mod ID, resource namespace, icon name, Mixin filenames, and the Fabric class-tweaker filename |
| `mod_name` | User-facing display name and JAR manifest metadata |
| `mod_package` | Fully qualified entrypoint references and Mixin package declarations in processed resources |
| `mod_group_id` | Gradle group and Maven publication coordinate; it may equal `mod_package`, but has a separate responsibility |
| `mod_version` | Mod and publication version |

`fabric.mod.json` and the Mixin configurations intentionally contain placeholders such as `${mod_id}` and `${mod_package}`. IntelliJ IDEA may mark these unexpanded values as unresolved in the source files. This is expected: Gradle expands them during resource processing so the generated metadata remains synchronized with `gradle.properties`.

Changing `mod_package` does **not** rename Java directories, Java `package` declarations, imports, or ServiceLoader descriptor filenames. Those are source-level identifiers and must be renamed with IntelliJ refactoring or manually.

## Creating a Project

1. Copy or clone the repository and set `rootProject.name` in `settings.gradle`.
2. Update the shared values in `gradle.properties`, especially `mod_id`, `mod_name`, `mod_package`, `mod_group_id`, version ranges, and project URLs.
3. Keep `Constants.MOD_ID` and `Constants.MOD_NAME` synchronized with `mod_id` and `mod_name`. `Constants.MOD_ID` remains a Java compile-time constant because NeoForge annotations require one.
4. Refactor the example Java package `io.github.simonxwei.template` to match `mod_package`. The fixed `io.github.simonxwei` prefix is intentional for this personal template.
5. Rename Java types such as `TemplateMod` when the new project should use a different class prefix, then update the corresponding type suffixes in `fabric.mod.json`.
6. Rename the physical resource files whose names are derived from `mod_id`:
   - `template.png`
   - `template.mixins.json`
   - `template.fabric.mixins.json`
   - `template.fabric.client.mixins.json`
   - `template.neoforge.mixins.json`
   - `template.classtweaker`
7. When changing the Java package, rename both descriptor files under `META-INF/services` and update the provider class stored inside each file. Gradle does not rename these source files from `mod_package`.
8. Delete or regenerate stale data under `neoforge/src/generated/resources` after changing the mod ID, then reload Gradle.
9. Open the repository root in IntelliJ IDEA with Java 25 as both the Gradle JVM and Project SDK, and test one client run for each loader.

`META-INF/accesstransformer.cfg` keeps its standard NeoForge path and does not need a mod-ID-specific filename.

## Development Layout

Most implementation code belongs in `common`; it must not reference Fabric or NeoForge APIs. Loader-specific registration, lifecycle hooks, integrations, and client behavior belong in the relevant loader project.

Fabric uses separate source sets:

```text
fabric/src/main/      Fabric code available on both physical sides
fabric/src/client/    Fabric client-only code and resources
```

Mixin configurations are distributed by responsibility:

```text
common/src/main/resources/template.mixins.json
fabric/src/main/resources/template.fabric.mixins.json
fabric/src/client/resources/template.fabric.client.mixins.json
neoforge/src/main/resources/template.neoforge.mixins.json
```

The Mixin `package` fields are expanded from `mod_package`. The configuration filenames are expanded from `mod_id` where they are referenced, but the physical files must still be renamed when creating a project.

## Access Changes

Access changes required by shared code are declared in both loader formats and stored in `common`:

```text
common/src/main/resources/template.classtweaker
common/src/main/resources/META-INF/accesstransformer.cfg
```

The Fabric class tweaker and NeoForge Access Transformer should contain semantically equivalent entries whenever `common` depends on the access change. Fabric consumes the class tweaker through Loom. Both `common` and `neoforge` apply the same Access Transformer because each compiles the common Java sources in its own Gradle compilation environment.

After editing either file, reload the Gradle project. Fabric entries can additionally be checked with:

```shell
./gradlew :fabric:validateAccessWidener
```

## Common Tasks

```shell
./gradlew build
./gradlew :common:build
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew publishToMavenLocal
./gradlew publish
```

All three projects form one publication set. An unqualified root `publish` or `publishToMavenLocal` task runs the matching publication task in `common`, `fabric`, and `neoforge`. By default, `publish` writes to the root `repo/` directory; set the `local_maven_url` environment variable to redirect that repository.

## Template Sources

This repository is a personal derivative of [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template). Its loader-specific build files are also organized by comparison with the contemporary Fabric and NeoForge standalone templates while retaining centralized shared metadata and versions.

The upstream MultiLoader Template is released under [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/). Attribution is retained to preserve provenance. This repository is not an official Fabric, NeoForge, or upstream MultiLoader project.

## License

This template is released under CC0 1.0 Universal. See [`LICENSE`](LICENSE).

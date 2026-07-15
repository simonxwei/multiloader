# multiloader

A personal Gradle project template for developing the same Minecraft mod on Fabric and NeoForge while keeping shared code in an independently compilable `common` project.

The template currently targets Minecraft 26.2 and Java 25. Version numbers, compatibility ranges, mod metadata, and build-tool versions are centralized in `gradle.properties` so that the two loader projects do not drift apart.

## Design

- `common` contains loader-independent code and is compiled against NeoForm vanilla mode.
- `fabric` follows the current Fabric template structure and separates `main` and `client` source sets.
- `neoforge` follows the current NeoForge ModDevGradle template structure.
- `build-logic` contains only build behavior shared by all projects and the source-merging behavior required by the multiloader layout.
- `common`, `fabric`, and `neoforge` use the `io.github.mcgradleconventions.loader` attribute so compatible dependency variants can be selected consistently.
- All three projects form one publication set. Running an unqualified root `publish` or `publishToMavenLocal` task publishes the matching publication from every subproject.

## Project Structure

```text
.
├── build-logic/    Shared Gradle convention plugins
├── common/         Loader-independent Java sources and resources
├── fabric/         Fabric entrypoints, APIs, resources, and client sources
└── neoforge/       NeoForge entrypoints, APIs, resources, and run settings
```

The loader projects compile and package the Java sources and resources exported by `common`. The final Fabric and NeoForge JARs are therefore independently installable; players do not install the `common` JAR.

## Getting Started

1. Copy or clone the repository under the new project name.
2. Set `rootProject.name` in `settings.gradle`.
3. Update the shared values in `gradle.properties`, especially:
   - `mod_id`
   - `mod_name`
   - `mod_version`
   - `mod_group_id`
   - `mod_package`
   - project URLs and the supported version ranges
4. Rename the final package segment under `io.github.simonxwei` and update the example Java classes.
5. Rename the fixed `template` resource names and their references when changing the mod ID:
   - `template.mixins.json`
   - `template.fabric.mixins.json`
   - `template.fabric.client.mixins.json`
   - `template.neoforge.mixins.json`
   - `template.classtweaker`
6. Open the repository root in IntelliJ IDEA and use a Java 25 Gradle JVM and Project SDK.
7. Reload the Gradle project and run one client configuration for each loader.

The `io.github.simonxwei` namespace is intentionally fixed because this is a personal template. Only the project-specific final package segment normally needs to change.

## Naming

The template intentionally uses different names for different layers:

- `multiloader` is the lowercase repository and Gradle project name.
- `template` is the lowercase example mod ID and resource namespace.
- `Template` is the example mod display name.
- `TemplateMod` and related PascalCase names are example Java types.

The README title follows the repository name rather than `mod_name`. When creating a mod, rename the project independently from the example mod ID, display name, and Java types as appropriate.

## Development

Most implementation code belongs in `common`. Code in `common` must not reference Fabric or NeoForge APIs. Loader-specific registration, lifecycle hooks, integrations, and client behavior belong in the corresponding loader project.

Fabric uses these source sets:

```text
fabric/src/main/      Fabric code available on both physical sides
fabric/src/client/    Fabric client-only code and resources
```

Mixin configurations are distributed as follows:

```text
common/src/main/resources/template.mixins.json
fabric/src/main/resources/template.fabric.mixins.json
fabric/src/client/resources/template.fabric.client.mixins.json
neoforge/src/main/resources/template.neoforge.mixins.json
```

The NeoForge-specific configuration contains both `mixins` and `client` lists because NeoForge does not use the Fabric client source-set split here.

Access changes used by shared code are also stored in `common`, but each loader consumes its own format:

```text
common/src/main/resources/template.classtweaker
common/src/main/resources/META-INF/accesstransformer.cfg
```

The Fabric class tweaker and NeoForge Access Transformer should contain semantically equivalent entries whenever shared code depends on the access change. `common` and `neoforge` both apply the shared Access Transformer because they compile the common Java sources in separate Gradle compilation environments. Fabric applies the class tweaker through Loom and declares it in `fabric.mod.json`.

After changing either file, reload the Gradle project. Fabric entries can additionally be checked with `./gradlew :fabric:validateAccessWidener`.

## Common Tasks

```shell
./gradlew build
./gradlew :common:build
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew publishToMavenLocal
./gradlew publish
```

By default, `publish` writes the three publications to the root `repo/` directory. Set the `local_maven_url` environment variable to redirect the shared publication repository.

## Template Sources

This repository is a modified personal derivative of [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template). Its loader-specific build files have also been reorganized by comparison with the contemporary Fabric and NeoForge standalone templates, while retaining the useful MultiLoader design of centralized versions and metadata.

The upstream MultiLoader Template is released under [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/). Attribution is included here to preserve provenance and acknowledge the original work, rather than because CC0 requires attribution.

This repository is not an official Fabric, NeoForge, or upstream MultiLoader project.

## License

This template is released under CC0 1.0 Universal. See [`LICENSE`](LICENSE).

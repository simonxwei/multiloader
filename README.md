<img src="common/src/main/resources/template.png" width="128" alt="multiloader icon">

# multiloader

**A personal Minecraft template for developing one mod on Fabric and NeoForge** with shared code in an independently compilable `common` project. It uses Java 25.

This project is derived from [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template). See the upstream README for the general IntelliJ IDEA setup and the common-versus-loader development model. This README documents only the conventions and differences specific to this repository.

---

## Projects

| Project       | Purpose                                                                                   |
|---------------|-------------------------------------------------------------------------------------------|
| `build-logic` | Shared Gradle conventions and common source/resource wiring                               |
| `common`      | Loader-independent code, common Mixins, and shared access declarations                    |
| `fabric`      | Fabric entrypoints, APIs, metadata, data generation, and separate main/client source sets |
| `neoforge`    | NeoForge entrypoints, runs, metadata generation, configuration, and data generation       |

Most mod code belongs in `common`, which must not reference Fabric or NeoForge APIs. Loader-specific registration, lifecycle hooks, integrations, and client behavior belong in the corresponding loader project.

## Creating a Project

1. Import the repository as a Gradle project with Java 25, following the upstream setup guide.
2. Set `rootProject.name` in `settings.gradle`, then update versions, compatibility ranges, URLs, and mod fields in `gradle.properties`.
3. Refactor `io.github.simonxwei.template` to match `mod_package`, rename the `TemplateMod` Java types as needed, and keep `Constants.MOD_ID` and `Constants.MOD_NAME` synchronized with `mod_id` and `mod_name`.
4. Rename resources derived from `mod_id`: the icon, four Mixin JSON files, and the Fabric class-tweaker file.
5. When changing the Java package, rename both files under `META-INF/services` and update the provider class inside each file.
6. Delete or regenerate stale files under `neoforge/src/generated/resources`, reload Gradle, and test both clients.

| Property       | Purpose                                                                                                      |
|----------------|--------------------------------------------------------------------------------------------------------------|
| `mod_id`       | Loader ID, resource namespace, icon name, Mixin filenames, and class-tweaker filename                        |
| `mod_name`     | User-facing display name and manifest metadata                                                               |
| `mod_package`  | Entrypoint references and Mixin package declarations in processed resources                                  |
| `mod_group_id` | Gradle group and Maven publication coordinate                                                                |
| `mod_version`  | Mod metadata and release version; combined with `minecraft_version` for the Gradle/Maven publication version |

For compatibility with both loaders, `mod_id` should match `[a-z][a-z0-9_]{1,63}`.

Metadata and Mixin JSON files intentionally contain placeholders such as `${mod_id}` and `${mod_package}`. IntelliJ IDEA may mark them as unresolved before Gradle processes the resources; do not replace them with hardcoded values merely to remove the warning.

Changing `mod_package` does not rename Java directories, package declarations, imports, class names, or ServiceLoader descriptors. Use IDEA refactoring for those source-level identifiers.

## Mixins and Access Changes

Mixin configurations are separated by responsibility:

```text
common/src/main/resources/template.mixins.json
fabric/src/main/resources/template.fabric.mixins.json
fabric/src/client/resources/template.fabric.client.mixins.json
neoforge/src/main/resources/template.neoforge.mixins.json
```

Shared access changes are stored in `common` in both loader formats:

```text
common/src/main/resources/template.classtweaker
common/src/main/resources/META-INF/accesstransformer.cfg
```

When common code depends on an access change, the Fabric class tweaker and NeoForge Access Transformer must contain semantically equivalent entries. The `common` and `neoforge` Gradle projects both read the same AT because each compiles the shared sources in a different environment; target validation is enabled in both. Reload Gradle after changing either access file.

Validate the Fabric class tweaker with:

```shell
./gradlew :fabric:validateAccessWidener
```

## Build and Publish

```shell
./gradlew build
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew publishToMavenLocal
./gradlew publish
```

Build artifacts are written to `common/build/libs`, `fabric/build/libs`, and `neoforge/build/libs`.

The root publication commands publish `common`, `fabric`, and `neoforge`. By default, `publish` writes to the root `repo/` directory and can be redirected with `local_maven_url`. `publishToMavenLocal` writes to the current user's Maven local repository, normally `~/.m2/repository` or `%USERPROFILE%\.m2\repository`.

## Releases

Development continues on `dev`. Each released Minecraft version is recorded on its own version branch and tagged as `mc<minecraft_version>-<mod_version>`. See [`RELEASING.md`](RELEASING.md) for the release checklist and maintenance policy.

## Source and License

This is an independent derivative, not an official Fabric, NeoForge, or upstream MultiLoader project. Loader-specific Gradle configuration also follows the contemporary Fabric and NeoForge standalone templates where appropriate.

The upstream MultiLoader Template and this repository use [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/). See [`LICENSE`](LICENSE).

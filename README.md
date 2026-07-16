<img src="common/src/main/resources/template.png" width="128">

# multiloader

**A custom Minecraft 26.2 template for developing one mod on Fabric and NeoForge** with shared code in an independently compilable `common` project. It uses Java 25.

Shared versions and mod metadata are kept in `gradle.properties`. Fabric- and NeoForge-specific Gradle DSL remains in the corresponding loader project so it can still be compared with the official templates.

---

## Projects

| Project | Purpose |
| --- | --- |
| `build-logic` | Shared Gradle conventions and common source/resource wiring |
| `common` | Loader-independent code, common Mixins, and shared access declarations |
| `fabric` | Fabric entrypoints, APIs, metadata, data generation, and separate main/client source sets |
| `neoforge` | NeoForge entrypoints, runs, metadata generation, configuration, and data generation |

Most mod code belongs in `common`, which must not reference Fabric or NeoForge APIs. Loader-specific registration, lifecycle hooks, integrations, and client behavior belong in the relevant loader project.

## Creating a Project

1. Set `rootProject.name` in `settings.gradle` to the new project folder name.
2. Update the versions, compatibility ranges, URLs, and mod fields in `gradle.properties`.
3. Refactor `io.github.simonxwei.template` to match `mod_package`. The fixed `io.github.simonxwei` prefix is intentional for this personal template.
4. Keep `Constants.MOD_ID` and `Constants.MOD_NAME` synchronized with `mod_id` and `mod_name`. `Constants.MOD_ID` remains a Java compile-time constant for loader annotations.
5. Rename Java types such as `TemplateMod` when using a different class prefix, then update the corresponding class suffixes in `fabric.mod.json`.
6. Rename the resource files derived from `mod_id`:
   - `template.png`
   - `template.mixins.json`
   - `template.fabric.mixins.json`
   - `template.fabric.client.mixins.json`
   - `template.neoforge.mixins.json`
   - `template.classtweaker`
7. When changing the Java package, rename both files under `META-INF/services` and update the provider class written inside each file.
8. Delete or regenerate stale files under `neoforge/src/generated/resources`, reload Gradle, and test both clients.

The key fields have separate responsibilities:

| Property | Purpose |
| --- | --- |
| `mod_id` | Loader ID, resource namespace, icon name, Mixin filenames, and class-tweaker filename |
| `mod_name` | User-facing display name and manifest metadata |
| `mod_package` | Entrypoint references and Mixin package declarations in processed resources |
| `mod_group_id` | Gradle group and Maven publication coordinate |
| `mod_version` | Mod metadata and release version; combined with `minecraft_version` for the Gradle/Maven publication version |

For compatibility with both loaders, `mod_id` should match `[a-z][a-z0-9_]{1,63}`.

Metadata and Mixin JSON files intentionally contain placeholders such as `${mod_id}` and `${mod_package}`. IntelliJ IDEA may mark them as unresolved before Gradle processes the resources. This is expected and should not be fixed by hardcoding the generated values.

Changing `mod_package` does not rename Java directories, `package` declarations, imports, class names, or ServiceLoader descriptors; those source-level identifiers must still be refactored.

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

When `common` depends on an access change, the Fabric class tweaker and NeoForge Access Transformer must contain semantically equivalent entries. A change used only by loader-specific code needs only that loader's declaration.

Both `common` and `neoforge` apply the shared Access Transformer because each compiles the common Java sources in its own Gradle environment. Reload Gradle after changing either access file. Fabric entries can also be checked with:

```shell
./gradlew :fabric:validateAccessWidener
```

## Common Commands

```shell
./gradlew build
./gradlew :common:build
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew publishToMavenLocal
./gradlew publish
```

The root publication commands run the corresponding tasks for `common`, `fabric`, and `neoforge`. By default, `publish` writes to the root `repo/` directory; set `local_maven_url` to redirect it.

## Versioning and Releases

The latest Minecraft development line uses `dev`. A released template is kept on a branch named after its Minecraft version, such as `26.2`. Release tags combine the Minecraft version and the independent mod version:

```text
mc26.2-1.0.0
```

Unreleased work remains untagged on `dev`. This template does not use alpha, beta, or release-candidate suffixes, and one tag represents the shared common, Fabric, and NeoForge release. Version branches record the template released for that Minecraft version; they do not imply an obligation to keep loader or dependency versions continuously updated. See [`RELEASING.md`](RELEASING.md) for the IDEA and GitHub release workflow.

## Sources and License

This repository is a personal derivative of [Jaredlll08's MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) and also follows the contemporary Fabric and NeoForge standalone templates for loader-specific configuration. It is not an official Fabric, NeoForge, or upstream MultiLoader project.

The upstream MultiLoader Template and this template are released under [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/). See [`LICENSE`](LICENSE).

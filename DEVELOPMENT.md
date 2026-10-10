# Development

This document defines reusable development conventions and source boundaries for a multiloader Minecraft project.
Template-specific setup steps apply when creating a new mod; domain architecture belongs to the individual project.

---

## Version and Identity Policy

- Keep **specific dependency versions** in the Gradle configuration for the current branch. Versions necessarily differ between Minecraft targets.
- Do not bake a specific Minecraft release into reusable documentation, naming rules, or explanatory comments. Use Gradle property names and placeholders instead.
- Prefer the project's configured `mod_id`, Java package, and display name to hard-coded identities when configuring build metadata.
- Keep `mod_id` (runtime identity), Java package (type namespace), and project prefix (source-level naming) conceptually distinct.

## Prerequisites

- A JDK compatible with `java_version` in `gradle.properties`.
- A Gradle environment configured through the included wrapper; no separate Gradle installation is required.
- An IDE with Java and Gradle support. IntelliJ IDEA is suitable, but not required.

Use the wrapper (`./gradlew` on Unix-like systems, `gradlew.bat` on Windows) for consistent builds.

## Projects and Source Boundaries

```text
build-logic/           Reusable Gradle conventions
common/src/main/       Shared code safe on both physical sides
common/src/client/     Shared client-only code
fabric/src/main/       Fabric common-side integration
fabric/src/client/     Fabric client integration and data generation
neoforge/src/main/     NeoForge integration and Dist-aware client initialization
```

- `common/src/main` must not reference loader APIs or client-only Minecraft types.
- `common/src/client` can use Minecraft client types but must not depend on Fabric or NeoForge APIs. Its *source set* enforces the physical-side boundary; avoid loader-specific client annotations in this shared code.
- Fabric uses a client source set. NeoForge compiles shared client code into its universal output and controls initialization through its client-only entrypoints or events.
- Loader differences belong in platform projects or their platform service implementations, rather than leaking into common code.

## Creating a Mod from the Template

1. Set `rootProject.name` in `settings.gradle`.
2. Configure `minecraft_*`, `java_version`, `fabric_*`, `neoforge_*`, `mixin_*`, and `mod_*` properties in `gradle.properties`.
3. Refactor the Java package and `Template*` classes using the IDE, including declarations and imports. The three module roles use `TemplateCommonMod`, `TemplateFabricMod`, and `TemplateNeoForgeMod`; client and data-generation entrypoints use the same module qualifier. Refactor these together with loader metadata.
4. Synchronize source constants (such as `MOD_ID` and `MOD_NAME`) with the corresponding Gradle properties. Changing `mod_package` **does not** refactor Java source files. CI reads `java_version` directly from `gradle.properties`.
5. Rename both `META-INF/services` descriptor filenames and their provider-class contents to the final package.
6. Rename Mixin configuration files and the Fabric class-tweaker file to use the final mod ID. Update loader metadata references and Mixin `package` values accordingly.
7. Review or replace all template example code and assets, including `icon.png`, `banner.png`, and `src/generated/resources`.
8. Review `.gitkeep` placeholders. Keep only those needed to preserve intentional empty directories; remove the others when adapting the template.
9. Refresh Gradle, validate both loader builds, and run physical-client and dedicated-server tests.
10. Replace the template README with an actual description of the mod; keep this file and `RELEASING.md` if their workflow remains applicable.

### Directory Placeholders

Git does not track empty directories.
Use `.gitkeep` to preserve **intentional empty directories**, not only Mixin packages.
Add placeholders where an otherwise empty package or source directory is useful to the project's structure; do not create directories merely for visual symmetry.
When adapting the template, delete placeholders for directories you no longer need.
Remove a placeholder once real tracked files occupy its directory.

The current template reserves the following Mixin packages:

- `common/src/main/java/.../mixin` and `common/src/client/java/.../client/mixin`;
- `fabric/src/main/java/.../mixin` and `fabric/src/client/java/.../client/mixin`;
- `neoforge/src/main/java/.../mixin` (one package for its common and client Mixin declarations).

These packages match the corresponding Mixin JSON `package` fields.
`.gitkeep` is a Git directory marker, not a Java source or Mixin declaration.
Do not list it in Mixin configurations.
The shared Gradle conventions exclude `**/.gitkeep` from `Jar` tasks (including `sourcesJar`)
and `Javadoc` tasks.
The placeholders remain tracked by Git, but are neither packaged nor treated as Javadoc sources.
Avoid placing placeholders inside resource roots such as `src/main/resources` or `src/generated/resources`
unless their handling is explicitly accounted for by the build.

### Resource and Metadata Paths

Build scripts expand selected `${mod_id}` and `${mod_package}` placeholders in metadata and Mixin JSON. After choosing the final identity, concrete paths can improve IDE navigation:

- Mixin configuration filenames referenced by Fabric and NeoForge metadata;
- Fabric class-tweaker filename and metadata reference;
- Fabric entrypoint names, including data-generation entrypoints;
- Mixin JSON `package` values;
- ServiceLoader descriptor paths and contents.

Keep the class tweaker and Access Transformer **semantically equivalent** when shared code depends on access changes.
They are different loader-specific formats for the same intended access changes.

### Configuration Properties

| Property family                           | Responsibility                                                |
|-------------------------------------------|---------------------------------------------------------------|
| `minecraft_*`, `java_version`, `username` | Target game and local development environment                 |
| `fabric_*`                                | Fabric tooling, loader, and API                               |
| `neoforge_*`                              | NeoForge tooling, transformations, runtime, and compatibility |
| `mixin_*`                                 | Mixin requirements and MixinExtras                            |
| `mod_*`                                   | Identity, artifacts, and mod metadata                         |

NeoForge properties describe separate layers: `neoforge_moddev_version` selects the development plugin, `neoforge_neoform_version` the development artifact, `neoforge_version` the platform, and `neoforge_javafml_version_range` the language-loader compatibility range.

## Code Conventions

These conventions prioritize maintainability, source navigation, and a consistent relationship with vanilla Minecraft.
They are **defaults**, not reasons to create unnecessary classes or duplicate vanilla behavior.

### Naming

- **Vanilla-parallel types:** prepend the project's unabridged class prefix to the *complete* corresponding vanilla type name; do not silently remove or rename parts of the original name. For example, `RandomState` becomes `<ProjectPrefix>RandomState` and `VanillaRegistries` becomes `<ProjectPrefix>VanillaRegistries`.
- **Domain counterparts:** a project may replace a vanilla domain term with its own domain term **without adding a second project prefix**, but only when the concepts truly correspond. Record project-specific mappings in that project's documentation rather than in the generic template.
- **Multiloader entrypoint roles:** use `<ProjectPrefix><Module>Mod` for the module-level initialization classes, with `<Module>` equal to `Common`, `Fabric`, or `NeoForge` (for example, `TemplateCommonMod`, `TemplateFabricMod`, and `TemplateNeoForgeMod`). The Common class holds shared initialization logic; only the loader-specific classes are actual loader entrypoints. Apply the same module qualifier to client and data-generation entrypoint names, such as `TemplateFabricModClient` and `TemplateNeoForgeModDataGenerator`.
- **Parallel implementations across modules:** when the same vanilla type needs separate Common and loader-specific implementations, preserve the full vanilla class name and insert the module qualifier after the project prefix (for example, `<ProjectPrefix>CommonRandomState` and `<ProjectPrefix>FabricRandomState`). Do not add a module qualifier to unrelated classes that already have unambiguous roles.
- **Internal helpers:** avoid redundant project prefixes when their enclosing package and role already make the meaning clear; do not remove a prefix if doing so harms readability or creates import ambiguity.
- **Mixin classes:** name by target vanilla class followed by `Mixin`; do not name Mixin types after the implementation domain by default.
- **Injected members:** use the **full mod ID** and `$`, such as `modid$cachedValue` or `modid$getValue()`, for newly injected members and their bridge-interface methods. Use `@Unique` where appropriate. Members mapped to existing vanilla fields or methods (for example, `@Shadow`) must follow their mapping requirements rather than receiving an arbitrary prefix.
- **Ordinary Java members:** follow Java camelCase conventions; the Mixin `$` convention does not apply to ordinary project code.

The Java package, not the simple class name, provides JVM type isolation across mods.
A full mod ID on injected members matters because different mods can add members to the *same target class*.

### Javadoc

For now, Javadoc is a **source-navigation aid**, not a requirement to document every implementation detail:

- If a class has a clear vanilla counterpart, use only `@see` (that type) and `@author` at class level.
- If a method has a clear vanilla counterpart, use an inline `{@link ...}` to the corresponding method; use the full signature where needed to disambiguate overloads.
- If there is no vanilla counterpart, Javadoc is optional and usually omitted.
- Do not add class-level Javadoc to Mixin classes solely to duplicate the target already identified by `@Mixin`.
- References must point to real types and members in the currently targeted mappings; do not add placeholders that cannot resolve.

### Exceptions

- Choose the **exception type** based on the broken contract: `IllegalArgumentException` for invalid arguments, `IllegalStateException` for invalid object or lifecycle state, and `UnsupportedOperationException` for genuinely unsupported operations.
- Message strings use concise **English sentence case**, with **no trailing period** and no redundant `Error:` or exception-class prefix.
- State the violated condition and include an actual value or type when useful. Avoid fixed verbose sentence templates.
- Use simple argument names in `Objects.requireNonNull(value, "value")` when this already identifies the failure.
- Do not mask broken invariants with `null` fallbacks or unsupported-operation placeholders merely to keep the mod running.

Examples:

```java
throw new IllegalArgumentException("Material rule sequence must not be empty");
throw new IllegalStateException("Expected CustomState, got " + state.getClass().getName());
Objects.requireNonNull(source, "source");
```

### Modifiers

- Declare method and constructor **parameters `final` by default**, so intentional reassignment stands out when it occurs.
- Declare concrete **classes `final` by default** when inheritance is not part of the design. Do not force `final` on extension points, abstract classes, or types that must be inherited.
- Choose `static` for class-level responsibilities and use the least visibility that supports the required API. Do not add modifiers mechanically.
- A static utility class normally has a private constructor. Local-variable `final` is discretionary.

### Comments

- Keep comments explaining **non-obvious reasons, constraints, or cross-loader requirements**; avoid narrating self-explanatory Java or Gradle syntax.
- Template comments should not mention particular Minecraft release numbers or copy-pasted dependency examples with obsolete property names.
- Keep meaningful upstream attribution and compatibility rationale where they affect maintainability. Preserve such comments when turning the template into a mod; only revise comments that no longer describe the resulting code.
- Put short sentences on separate source lines. Wrap long comments at natural phrase boundaries without splitting identifiers, commands, or URLs.
- Preserve source-navigation links in property comments, including version-specific upstream links when they point to the relevant source.

### IDE and Gradle DSL Inspections

- Gradle and loader plugins expose dynamic Groovy DSL methods that IntelliJ IDEA may not infer correctly.
- Distinguish IDE-only inspections from actual Gradle compilation or configuration failures before modifying a working build script.
- Do not change configuration semantics or suppress warnings globally solely to silence an inspection.
- Add genuine technical terms to the IDE spellchecker dictionary when appropriate; do not rename valid Gradle tasks, properties, or DSL methods to satisfy spelling suggestions.

## Build, Data Generation, and Tests

Validate compilation, shared access configuration, and local publication:

```shell
./gradlew clean build :fabric:validateAccessWidener publishToMavenLocal --warning-mode all
```

Run clients and dedicated servers **individually**, stopping each cleanly:

```shell
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew :fabric:runServer
./gradlew :neoforge:runServer
```

An integrated single-player server does not replace testing the physical dedicated-server environment.
NeoForge's Gradle run configuration can forward standard input for console commands such as `stop`.

Use the relevant loader's data-generation tasks when a project adds providers.
Check generated output before committing it; remove stale template resources and keep generated data in the intended source directory.

Build outputs are located under each project's `build/libs`.
The published `common` component contains the shared main sources; the two loader builds also include the physically separated shared client sources.

## Publication and Release Workflows

The root publication tasks cover all three projects.
`publishToMavenLocal` writes to the user Maven-local repository (typically `~/.m2/repository`); `publish` writes to the project-root `repo/` directory by default, overridable with the `local_maven_url` environment variable.

See [RELEASING.md](RELEASING.md) for reusable release verification, branch management, and publication procedures for both the template and derived mods.

# Development Guide

Use the JDK specified by `java_version` and the included wrapper from the repository root.
On Windows, replace `./gradlew` with `gradlew.bat`.
See [Releasing](RELEASING.md) for versions, artifacts, and publication.

---

## Create a Mod

| Update                                 | Locations                                                                   |
|----------------------------------------|-----------------------------------------------------------------------------|
| Identity, dependencies, links, license | `settings.gradle`, `gradle.properties`, `LICENSE`                           |
| Java package and `Template*` types     | Refactor across all three modules                                           |
| Source identity                        | `TemplateConstants.MOD_ID` and `MOD_NAME`                                   |
| Entrypoints and resources              | `fabric.mod.json`, `neoforge.mods.toml`, Mixin JSON, class tweaker, artwork |
| Service discovery                      | `META-INF/services` filenames and provider names                            |
| Examples and documentation             | Data providers, generated outputs, artwork, and guides                      |

| Stage                       | Path references                                                                  |
|-----------------------------|----------------------------------------------------------------------------------|
| Published template          | Keep `${mod_id}`, `${mod_package}`, `${mod_icon}`, and `${mod_banner}` expansion |
| Derived mod, after renaming | Use actual class names, Mixin packages, and resource paths for IDE navigation    |

Properties do not rename Java types, resource files, or service descriptors.
For example, changing `mod_id` to `example` also requires renaming `template.mixins.json` to `example.mixins.json` and aligning source constants.
After resolving path references, remove unused path properties and expansion-map entries; keep metadata and version expansion.
Edit providers before regenerating data. Remove obsolete generated examples.

**Check:** Search for old identifiers and broken references, refresh Gradle, then [build and test](#build-and-test).
`mod_version=1.0.0` is a starter mod version, independent of template release tags.

## Make a Change

```shell
git switch dev
git pull --ff-only origin dev
git switch -c fix/icon-path
# Edit and run the applicable checks.
git diff --check
git add -A
git diff --cached
git commit -m "fix(resources): correct icon path"
git push -u origin fix/icon-path
```

Replace example names. Keep each commit coherent; use a PR when review or repository rules require it.
Review the diff and CI before merging, squash intermediate commits when appropriate, and delete integrated temporary branches.
Direct updates require repository rules to permit them.

### Commit Messages

Use lightweight [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```text
<type>(<optional-scope>): <imperative description>
```

| Type                | Use for                                        |
|---------------------|------------------------------------------------|
| `feat` / `fix`      | Functionality / bug fixes                      |
| `refactor` / `perf` | Restructuring / performance                    |
| `docs` / `test`     | Documentation / tests                          |
| `build` / `ci`      | Build configuration, dependencies / automation |
| `chore`             | Other maintenance                              |

```text
build(deps): update Fabric API
refactor(lang): group language codes
docs: clarify template setup
```

Add a body for non-obvious reasoning; use `!` or a `BREAKING CHANGE:` footer for breaking changes.
Commit types do not automatically determine release versions.

### Pull Requests and Issues

| Item       | Include                                                            |
|------------|--------------------------------------------------------------------|
| PR         | What changed, why, linked issues, checks actually performed        |
| Bug report | Game/loader versions, reproduction, expected/actual behavior, logs |
| Proposal   | Goal and acceptance conditions                                     |

Close resolved issues; routine edits need no separate issue.

## Build and Test

```shell
./gradlew clean build :fabric:validateAccessWidener publishToMavenLocal --warning-mode all
```

Run clients and dedicated servers **one at a time**, verifying startup and clean shutdown:

```shell
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew :fabric:runServer
./gradlew :neoforge:runServer
```

A build or single-player session does not replace dedicated-server testing.
After provider changes, regenerate and inspect resources:

```shell
./gradlew :fabric:runDatagen
./gradlew :neoforge:runData
```

| Output        | Location                            |
|---------------|-------------------------------------|
| Fabric data   | `fabric/src/main/generated/`        |
| NeoForge data | `neoforge/src/generated/resources/` |
| JARs          | Each module's `build/libs/`         |

NeoForge's server run accepts input such as `stop`.
CI builds, validates access changes and Maven-local publication, and uploads artifacts on pushes and PRs.
Publication is separate.

## Synchronize Minecraft Branches

| Compatible changes and target history   | Method         |
|-----------------------------------------|----------------|
| Behind `dev`, no independent commits    | Fast-forward   |
| Independent commits on both branches    | Reviewed merge |
| Only selected changes apply             | Cherry-pick    |

Replace `MC_BRANCH` and `COMMIT_SHA`; push source commits first.

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
```

Choose `git merge --ff-only origin/dev` or `git cherry-pick COMMIT_SHA` as appropriate.
Review, build, and test, then `git push origin MC_BRANCH`.
For a required PR, create the temporary branch **from the destination branch**.
Cherry-pick creates new commits and does not adapt APIs automatically.
If fast-forward fails, inspect histories instead of force-pushing. Check the resulting commit and CI.

## Reference: Source Layout

| Location             | Responsibility                                             |
|----------------------|------------------------------------------------------------|
| `build-logic/`       | Shared Gradle conventions                                  |
| `common/src/main/`   | Loader-independent code for both physical sides            |
| `common/src/client/` | Shared client-only code                                    |
| `fabric/src/main/`   | Fabric common-side integration                             |
| `fabric/src/client/` | Fabric client integration and datagen entrypoint           |
| `neoforge/src/main/` | NeoForge integration with Dist-aware client initialization |

Common main must not import client-only types; neither Common source set may import loader APIs.
Fabric separates client sources; NeoForge packages shared client code in its universal JAR and guards initialization.

### Shared Definitions

Maintain each shared declaration once; adapt it at each loader's lifecycle point.

| Common                                   | Loader                           |
|------------------------------------------|----------------------------------|
| Registry entries and factories           | Registration API and timing      |
| Payload types, codecs, registration list | Registration and receivers       |
| Data-generation bootstrap and data       | Providers and output             |
| Shared translations                      | Output and platform-only entries |

The bundled language providers use this pattern:

```java
// Common: inside TemplateENUSLanguageProvider.add(...).
// provider.template$add("template.example", "Example");

// Fabric: inside generateTranslations(...).
TemplateENUSLanguageProvider.add(translationBuilder::add);
// NeoForge: inside addTranslations().
TemplateENUSLanguageProvider.add(this::add);
```

Use callbacks for declaration lists and `TemplateServices` for platform implementations called by Common.
Platform-specific entries, ordering, and side rules stay with each loader.
Language codes use `TemplateLanguage.Codes`, and shared translation keys use `TemplateLanguage.Keys`.
Keep NeoForge-only keys in `NeoForgeLanguageKeys`.
Configuration classes and hooks are placeholders; add options and any Fabric configuration dependency when the derived mod needs them.

### Configuration and Resources

| Properties                                | Purpose                                 |
|-------------------------------------------|-----------------------------------------|
| `minecraft_*`, `java_version`, `username` | Game target and development environment |
| `fabric_*`, `neoforge_*`                  | Loader, tooling, and compatibility      |
| `mixin_*`                                 | Mixin and MixinExtras requirements      |
| `mod_*`                                   | Identity, path expansion, and metadata  |

Keep dependency versions in Gradle configuration, not reusable comments.
ModDevGradle, NeoForm, NeoForge, and JavaFML properties describe separate layers.
Keep class-tweaker and Access Transformer entries semantically equivalent for shared access changes.
Use `.gitkeep` for intentional empty directories; remove it when files arrive. The build excludes these placeholders from JARs and Javadoc.

## Reference: Code Conventions

### Naming

| Case                                      | Pattern / example                                              |
|-------------------------------------------|----------------------------------------------------------------|
| Common initialization                     | `TemplateModCommon`, `TemplateModCommonClient`                 |
| Loader entrypoint, per module             | `TemplateMod`, `TemplateModClient`, `TemplateModDataGenerator` |
| Vanilla counterpart                       | Prefix the full name: `TemplateRandomState`                    |
| True domain counterpart                   | Replace the domain term; document that mod's mapping           |
| Parallel module implementation            | Keep the full vanilla name: `TemplateCommonRandomState`        |
| Internal helper                           | Omit redundant prefixes when the package and role are clear    |
| Mixin                                     | Target name + `Mixin`                                          |
| New injected member / bridge              | `template$cachedValue`; use `@Unique` as needed                |
| Project-defined interface instance method | `template$add`, `template$getPlatformName`                     |
| Other Java member                         | Standard camelCase                                             |

Loader entrypoints with identical names belong to separate module outputs.
This project prefixes its own interface instance methods with the full mod ID, including their implementations; this is a project convention.
Preserve upstream interface method names and `@Shadow` targets.
Use small functional interfaces for callbacks, and `public static final` nested classes with private constructors for utility groups.
Do not add wrappers merely for naming symmetry.

### Comments and Javadoc

| Purpose                | Example / rule                                                         |
|------------------------|------------------------------------------------------------------------|
| Customization point    | `// Add shared translation entries here.`                              |
| Non-obvious constraint | `// Load platform implementations without loader APIs in common code.` |
| Upstream navigation    | `@see net.minecraft.client.ClientBootstrap`                            |
| Method navigation      | `{@link TemplateConstants#id(String)}`                                 |
| File syntax            | Java/Groovy: `//`; YAML/properties: `#`                                |
| Properties             | `#` section heading; `##` property note                                |

Explain intent, constraints, and replacement points; omit code narration and routine Javadoc on helpers or Mixins.
Use brief English sentences ending in periods; start sentences on separate lines and wrap naturally.
Preserve useful upstream URLs without trailing punctuation. Keep instructions out of JSON, service descriptors, and generated files.
Explicit separators such as `MOD_ID + "." + "configuration"` and `MOD_NAME + " " + "Configs"` are acceptable for readability.

### Exceptions and Diagnostic Logs

Format examples may use placeholder types and members:

| Reference                | Example message                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Parameter / local        | `"serviceClass"` or `"version: expected=" + VERSION + ", actual=" + version` |
| Field                    | `"TemplateServices#PLATFORM: not initialized"`                               |
| Method                   | `"TemplateServices#load(Class): cannot load service"`                        |
| Shadow method            | `"TargetClass#method(int): mixin shadow method is unavailable"`              |
| Injected field           | `"TargetMixin#template$cachedValue: not initialized"`                        |
| Code name in explanation | `"provider: 'TemplatePlatformHelper' implementation required"`               |
| Nested type              | `"TemplateProvider.Entry#load(String): entry is unavailable"`                |

- **Locator:** use bare parameter/local names or the declaring type, including enclosing types. Methods use `Type#method(ParameterTypes)` with declared parameter types in order; omit names, modifiers, annotations, and generic type arguments. Use `()` for no arguments and qualify ambiguous type names. Shadows name the original declaration; injected members name the Mixin.
- **Message:** `Objects.requireNonNull` normally needs only the name. Add `: explanation` when useful; other exceptions need a clear reason. Start explanations lowercase, preserving code spelling; single-quote code names in explanations, not locators. Omit trailing periods and redundant `Error:` prefixes. Log values use `{}`; routine status logs may omit locators.
- **Behavior:** choose argument/state/unsupported-operation exceptions appropriately. Preserve checks, causes, log levels, and crash reports; avoid silent fallbacks and formatting helpers added only for style.
- **Upstream:** preserve wording and line breaks except necessary domain/value substitutions and the above locator, capitalization, quoting, and punctuation adjustments. Codec diagnostics and player messages need no forced locator.

### Modifiers

Use `final` for method/constructor parameters and concrete classes unless inheritance is intentional.
Local-variable `final` is optional. Use minimal visibility and private constructors for static utility classes.

**IDE:** Verify actual Gradle failures before changing valid Groovy DSL calls to satisfy inspections; avoid global suppression.

## Maven Publication

| Command                         | Destination                                                                    |
|---------------------------------|--------------------------------------------------------------------------------|
| `./gradlew publishToMavenLocal` | Local Maven repository, normally `~/.m2/repository`                            |
| `./gradlew publish`             | Project-root `repo/`, overridden by the `local_maven_url` environment variable |

Root tasks publish Common, Fabric, and NeoForge. Common contains shared main code; loader outputs also include applicable shared client code.
GitHub Releases and mod distribution sites require separate publication steps.

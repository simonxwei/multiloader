# Development Guide

Use the JDK specified by `java_version` and the included Gradle wrapper from the repository root.
On Windows, use `gradlew.bat` instead of `./gradlew`.
For versioning and releases, see [Releasing](RELEASING.md).

---

## Create a Mod

| Change                                 | Where                                                                                  |
|----------------------------------------|----------------------------------------------------------------------------------------|
| Project identity and metadata          | `settings.gradle`, `gradle.properties`                                                 |
| Java packages and `Template*` types    | `common`, `fabric`, `neoforge`                                                         |
| Interface methods and injected members | Replace `template$` with the new mod ID prefix                                         |
| Constants                              | `TemplateConstants.MOD_ID`, `MOD_NAME`                                                 |
| Entrypoints and resource references    | `fabric.mod.json`, `neoforge.mods.toml`, Mixin JSON, class tweaker, Access Transformer |
| Service discovery                      | `META-INF/services` filenames and provider names                                       |
| Resource namespaces and documentation  | Rename template identifiers and references where needed                                |

| Context           | Path references                                                                                    |
|-------------------|----------------------------------------------------------------------------------------------------|
| Reusable template | Keep `${mod_id}`, `${mod_package}`, `${mod_icon}`, `${mod_banner}` expansion                       |
| Derived mod       | Use literal Mixin packages, entrypoint classes, and class-tweaker paths; retain metadata expansion |

**Scope:** Change project identity and references only.
Preserve examples, artwork, generated data, build configuration, and structure unless a change is required or explicitly requested.

Properties cannot rename files or Java types.
After fixing paths, remove only unused path properties and expansion entries. Update identifiers in data providers before regenerating data.
Search for stale identifiers, review the diff, and [build and test](#build-and-test).

`mod_version=1.0.0` is a starter mod version, not a template release version.

## Source Layout

| Location             | Responsibility                                            |
|----------------------|-----------------------------------------------------------|
| `common/src/main/`   | Shared code for both physical sides                       |
| `common/src/client/` | Shared client-only code                                   |
| `fabric/src/main/`   | Fabric common-side integration                            |
| `fabric/src/client/` | Fabric client integration and data-generation entrypoint  |
| `neoforge/src/main/` | NeoForge integration and Dist-aware client initialization |
| `build-logic/`       | Shared Gradle conventions                                 |

Common must not import loader APIs; Common main must not import client-only types.
Fabric separates client sources; NeoForge guards client initialization in its universal JAR.

### Shared Definitions

Define shared content once in Common, then connect it at each loader's lifecycle point.

| Common                               | Fabric / NeoForge                |
|--------------------------------------|----------------------------------|
| Registries and factories             | Registration API and timing      |
| Payloads, codecs, registration lists | Registration and receivers       |
| Data-generation bootstrap and data   | Providers and output             |
| Translations                         | Output and platform-only entries |

```java
// Common: inside TemplateENUSLanguageProvider.add(...)
provider.template$add("template.example", "Example");

// Fabric: inside generateTranslations(...)
TemplateENUSLanguageProvider.add(translationBuilder::add);

// NeoForge: inside addTranslations()
TemplateENUSLanguageProvider.add(this::add);
```

Use callbacks for shared declaration lists and `TemplateServices` for platform implementations accessed by Common.
Keep platform-specific ordering, lifecycle, and side rules in loader modules.

Language codes and shared keys live in `TemplateLanguageCodes` and `TemplateLanguageKeys`;
NeoForge-only keys live in `NeoForgeLanguageKeys`. Configuration hooks are placeholders until needed.

### Configuration and Resources

| Properties                                | Purpose                                 |
|-------------------------------------------|-----------------------------------------|
| `minecraft_*`, `java_version`, `username` | Game target and development environment |
| `fabric_*`, `neoforge_*`                  | Loader and tooling versions             |
| `mixin_*`                                 | Mixin and MixinExtras compatibility     |
| `mod_*`                                   | Mod identity and metadata               |

Keep versions in Gradle configuration.
ModDevGradle, NeoForm, NeoForge, and JavaFML refer to distinct layers.
Match class-tweaker and Access Transformer access changes across loaders.

Use `.gitkeep` only for intentional empty directories; remove it when files arrive.
Builds exclude it from JARs and Javadoc.

## Code Conventions

### Naming

| Case                              | Convention / example                                           |
|-----------------------------------|----------------------------------------------------------------|
| Common entrypoints                | `TemplateModCommon`, `TemplateModCommonClient`                 |
| Loader entrypoints                | `TemplateMod`, `TemplateModClient`, `TemplateModDataGenerator` |
| Vanilla counterpart               | Full prefix: `TemplateRandomState`                             |
| Domain counterpart                | Replace the domain term; document the mapping                  |
| Parallel implementation           | Keep the full counterpart name: `TemplateCommonRandomState`    |
| Internal helper                   | Omit redundant prefixes when clear                             |
| Mixin class                       | Target name + `Mixin`                                          |
| Injected / new member             | `template$cachedValue`; use `@Unique` when needed              |
| Project interface instance method | `template$add`, `template$getPlatformName`                     |
| Other members                     | Standard Java naming                                           |

Identical loader entrypoint names are allowed in separate modules.
Prefix **project-defined interface instance methods and implementations** with the mod ID; preserve upstream interface methods and `@Shadow` names.
Use small callback interfaces and `public static final` nested utility groups with private constructors.
Avoid symmetry-only wrappers.

### Comments and Javadoc

| Use                              | Convention / example                           |
|----------------------------------|------------------------------------------------|
| Customization / constraint       | Brief `//` comment explaining *why* or *where* |
| Upstream source                  | `@see net.minecraft.client.ClientBootstrap`    |
| Method reference                 | `{@link TemplateConstants#id(String)}`         |
| Java / Groovy; YAML / properties | `//`; `#`                                      |
| Properties                       | `#` section; `##` property note                |

Use concise English sentences with periods; avoid narrating obvious code or adding routine Javadoc to helpers and Mixins.
Preserve upstream URLs. Do not put instructions in JSON, service descriptors, or generated files.
Explicit separators such as `MOD_ID + "." + "configuration"` are acceptable.

### Exceptions and Logs

| Locator                  | Example                                                         |
|--------------------------|-----------------------------------------------------------------|
| Parameter / local        | `"serviceClass"`                                                |
| Field                    | `"TemplateServices#PLATFORM: not initialized"`                  |
| Method                   | `"TemplateServices#load(Class): cannot load service"`           |
| Shadow                   | `"TargetClass#method(int): mixin shadow method is unavailable"` |
| Injected member          | `"TargetMixin#template$cachedValue: not initialized"`           |
| Nested type              | `"TemplateProvider.Entry#load(String): entry is unavailable"`   |
| Code name in explanation | `"provider: 'TemplatePlatformHelper' implementation required"`  |

- **Locator:** `Type#field` or `Type#method(ParameterTypes)`; include enclosing types when needed. Method types follow declaration order; omit parameter names, modifiers, annotations, and generic arguments. Use `()` for zero arguments; qualify ambiguous types. Shadows refer to the original class, injected members to the Mixin.
- **Message:** `requireNonNull` normally uses the bare parameter/field name. Add `: explanation` where useful; start explanations lowercase unless using a code name. Quote code names in explanations, not locators. No trailing periods or `Error:` prefixes.
- **Behavior:** Preserve appropriate exception type, cause, log level, and crash report. Log values use `{}`. Routine status logs, codec diagnostics, and player messages need no forced locator.
- **Upstream:** Preserve wording and line breaks except necessary domain/value and formatting substitutions. Avoid silent fallbacks or style-only helper methods.

### Modifiers

Use `final` for concrete classes and method/constructor parameters unless extension is intended.
Local `final` is optional. Prefer minimal visibility and private constructors for utility classes.

**IDE:** Verify Gradle errors before rewriting valid Groovy DSL for inspections; avoid global suppression.

## Build and Test

| Check                                             | Command                                                                                      |
|---------------------------------------------------|----------------------------------------------------------------------------------------------|
| Build, access validation, Maven-local publication | `./gradlew clean build :fabric:validateAccessWidener publishToMavenLocal --warning-mode all` |
| Fabric client / server                            | `./gradlew :fabric:runClient` / `./gradlew :fabric:runServer`                                |
| NeoForge client / server                          | `./gradlew :neoforge:runClient` / `./gradlew :neoforge:runServer`                            |
| Fabric / NeoForge data generation                 | `./gradlew :fabric:runDatagen` / `./gradlew :neoforge:runData`                               |

Run clients and dedicated servers **one at a time**; confirm startup and clean shutdown.
A build or single-player run does not replace a dedicated-server test.
NeoForge's server console accepts `stop`.

| Output                  | Path                                |
|-------------------------|-------------------------------------|
| Fabric generated data   | `fabric/src/main/generated/`        |
| NeoForge generated data | `neoforge/src/generated/resources/` |
| JAR files               | `<module>/build/libs/`              |

Inspect generated resources after provider changes.
CI builds, validates access changes, publishes to Maven local, and uploads artifacts on pushes and PRs; distribution is separate.

### Maven Publication

| Command                         | Destination                                                |
|---------------------------------|------------------------------------------------------------|
| `./gradlew publishToMavenLocal` | `~/.m2/repository`                                         |
| `./gradlew publish`             | Project `repo/`, or `local_maven_url` environment override |

Root tasks publish Common, Fabric, and NeoForge.
Common contains shared main code; loader outputs include applicable shared client code.
GitHub and mod distribution are separate.

## Git Workflow

### Make a Change

```shell
git switch dev
git pull --ff-only origin dev
git switch -c fix/icon-path
# Edit and test.
git diff --check
git add -A
git diff --cached
git commit -m "fix(resources): correct icon path"
git push -u origin fix/icon-path
```

Use coherent commits; review diff and CI.
Open a PR when required, squash temporary commits when appropriate, and remove merged temporary branches.
Direct pushes must follow repository rules.

### Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/): `<type>(<optional-scope>): <imperative description>`.

| Type                | Purpose                          |
|---------------------|----------------------------------|
| `feat` / `fix`      | Features / fixes                 |
| `refactor` / `perf` | Restructuring / performance      |
| `docs` / `test`     | Documentation / tests            |
| `build` / `ci`      | Dependencies, build / automation |
| `chore`             | Other maintenance                |

Examples: `build(deps): update Fabric API`; `refactor(lang): group language codes`.
Add a body for non-obvious changes; mark breaking changes with `!` or `BREAKING CHANGE:`.
Commit types do not set release versions automatically.

### Pull Requests and Issues

| Item     | Include                                                     |
|----------|-------------------------------------------------------------|
| PR       | What, why, linked issues, checks performed                  |
| Bug      | MC/loader versions, reproduction, expected vs. actual, logs |
| Proposal | Goal and acceptance conditions                              |

Close resolved issues; routine edits need no issue.

### Synchronize Minecraft Branches

| Branch state                                    | Method         |
|-------------------------------------------------|----------------|
| Target behind `dev` with no independent commits | Fast-forward   |
| Both branches contain independent commits       | Reviewed merge |
| Only selected changes apply                     | Cherry-pick    |

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
# Choose: git merge --ff-only origin/dev  OR  git cherry-pick COMMIT_SHA
git push origin MC_BRANCH
```

Push source commits first. For PRs, branch **from the destination branch**.
Review and test each port; cherry-pick does not adapt APIs.
If fast-forward fails, inspect history instead of force-pushing; check CI and resulting commits.

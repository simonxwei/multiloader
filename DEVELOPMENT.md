# Development Guide

Use the JDK in `java_version` and the Gradle wrapper from the project root (`gradlew.bat` on Windows).

For versioning and distribution, see [Releasing](RELEASING.md).

---

## Create a Mod

### Identity and Naming

| Identity                    | Template                       | Configuration / convention                                                                      |
|-----------------------------|--------------------------------|-------------------------------------------------------------------------------------------------|
| Project                     | `multiloader`                  | Repository, canonical root folder, `rootProject.name`, and README `# multiloader` heading match |
| Mod ID                      | `template`                     | `mod_id` and `TemplateConstants.MOD_ID`; independently chosen registry/resource namespace       |
| Mod display name            | `Template`                     | `mod_name` and `TemplateConstants.MOD_NAME`; independently chosen user-facing name              |
| Java package                | `io.github.simonxwei.template` | `mod_package`; update source packages, imports, and services                                    |
| Java class prefix           | `Template`                     | UpperCamelCase form of the Mod ID (not the display name)                                        |
| Interface / injected prefix | `template$`                    | Mod ID prefix for project-defined interface methods and injected members                        |
| Maven group                 | `io.github.simonxwei.template` | `mod_group_id`; independent of the project name                                                 |

Use the lowercase project identifier for the repository, canonical root folder, Gradle root project, and **plain** README H1 (`# project-id`);
local checkout folder names may differ. `mod_id`, `mod_name`, `mod_package`, and `mod_group_id` are separate settings.
Convert Mod IDs containing Java-invalid characters before using them in Java identifiers.

### Conversion Checklist

| Area      | Action                                                                                                                            |
|-----------|-----------------------------------------------------------------------------------------------------------------------------------|
| Java      | Rename `Template*` types, packages, `template$` members, and matching imports/references                                          |
| Constants | Match `TemplateConstants.MOD_ID` / `MOD_NAME` to their corresponding properties                                                   |
| Loaders   | Update Fabric entrypoints, NeoForge metadata, Mixin packages and file references, class tweaker and Access Transformer references |
| Services  | Update `META-INF/services` filenames and implementation class names                                                               |
| Resources | Update namespaces, generated-data providers, project URLs, and documentation references                                           |

| Context           | Resource path policy                                                                                         |
|-------------------|--------------------------------------------------------------------------------------------------------------|
| Reusable template | Keep `${mod_id}`, `${mod_package}`, `${mod_icon}`, `${mod_banner}` expansion                                 |
| Derived mod       | Use literal Mixin packages, entrypoint class names, and class-tweaker paths; retain metadata value expansion |

**Scope:** Only change identity and necessary references.
Keep existing examples, artwork, generated data, build configuration, and structure unless required or requested.
Do not change `mod_version=1.0.0` merely to initialize a project; it is a starter mod version, not a `multiloader` release version.

Properties do not rename Java types or files.
Remove only path-expansion entries made unused by conversion; update data providers before regeneration.
Review the diff, search for stale identifiers, then [build and test](#build-and-test).

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

Define content in Common and connect it at each loader's lifecycle point.

| Common                               | Fabric / NeoForge                |
|--------------------------------------|----------------------------------|
| Registries and factories             | Registration API and timing      |
| Payloads, codecs, registration lists | Registration and receivers       |
| Data-generation bootstrap and data   | Providers and output             |
| Translations                         | Output and platform-only entries |

```java
// Common: TemplateENUSLanguageProvider.add(...)
provider.template$add("template.example", "Example");

// Fabric: generateTranslations(...)
TemplateENUSLanguageProvider.add(translationBuilder::add);

// NeoForge: addTranslations()
TemplateENUSLanguageProvider.add(this::add);
```

Use callbacks for shared declarations and `TemplateServices` for platform implementations accessed from Common.
Keep lifecycle, ordering, and side rules loader-specific.

Shared language codes and keys live in `TemplateLanguageCodes` and `TemplateLanguageKeys`; platform-only keys live in `NeoForgeLanguageKeys`.
Configuration hooks remain placeholders.

### Configuration and Resources

| Properties                                | Purpose                                 |
|-------------------------------------------|-----------------------------------------|
| `minecraft_*`, `java_version`, `username` | Game target and development environment |
| `fabric_*`, `neoforge_*`                  | Loader and tooling versions             |
| `mixin_*`                                 | Mixin and MixinExtras compatibility     |
| `mod_*`                                   | Mod identity and metadata               |

Keep versions in Gradle configuration.
ModDevGradle, NeoForm, NeoForge, and JavaFML are distinct layers.
Match class-tweaker and Access Transformer access changes across loaders.

Use `.gitkeep` only for intentional empty directories; remove it once populated.
Builds exclude it from JARs and Javadoc.

## Code Conventions

### Naming

| Case                             | Convention / example                                           |
|----------------------------------|----------------------------------------------------------------|
| Common entrypoints               | `TemplateModCommon`, `TemplateModCommonClient`                 |
| Loader entrypoints               | `TemplateMod`, `TemplateModClient`, `TemplateModDataGenerator` |
| Vanilla counterpart              | Full prefix: `TemplateRandomState`                             |
| Domain counterpart               | Replace the domain term; document the mapping                  |
| Parallel implementation          | Full counterpart name: `TemplateCommonRandomState`             |
| Internal helper                  | Omit redundant prefixes when clear                             |
| Mixin class                      | Target name + `Mixin`                                          |
| Project-defined interface method | `template$add`, `template$getPlatformName`                     |
| Injected / new member            | `template$cachedValue`; `@Unique` when needed                  |
| Other members                    | Standard Java naming                                           |

Entry point names may repeat in different loader modules.
Prefix project-defined interface instance methods **and implementations**; preserve upstream method contracts and `@Shadow` names.
Use small callback interfaces and `public static final` nested utility groups with private constructors.
Avoid symmetry-only wrappers.

### Comments and Javadoc

| Context                              | Convention / example                                       |
|--------------------------------------|------------------------------------------------------------|
| README title                         | Plain `# multiloader`, no backticks around the name        |
| Markdown prose / `//` / `#` comments | Mark exact project IDs as inline code: `` `multiloader` `` |
| Javadoc project ID                   | `{@code multiloader}`                                      |
| Code constraint                      | Brief `//` explaining *why* or *where*                     |
| Upstream source                      | `@see net.minecraft.client.ClientBootstrap`                |
| Method reference                     | `{@link TemplateConstants#id(String)}`                     |
| Java / Groovy; YAML / properties     | `//`; `#`                                                  |
| Properties                           | `#` section; `##` property note                            |

Use concise English sentences with periods.
Avoid narrating obvious code or routine Javadoc for helpers and Mixins.
Preserve upstream URLs; do not add instructions to JSON, service descriptors, or generated files.
Explicit separators such as `MOD_ID + "." + "configuration"` are acceptable.
Mark **identifiers**, not every ordinary mention of a mod, with code formatting.

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

- **Locator:** `Type#field` or `Type#method(ParameterTypes)`; include enclosing types when needed. Follow declaration order; omit parameter names, modifiers, annotations, and generic arguments. Use `()` for zero arguments; qualify ambiguous types. Shadows refer to the original class, injected members to the Mixin.
- **Message:** `requireNonNull` normally uses the bare parameter/field name. Add `: explanation` where useful; start explanations lowercase unless using a code name. Quote code names in explanations, not locators. No trailing periods or `Error:` prefixes.
- **Behavior:** Preserve appropriate exception type, cause, log level, and crash report. Log values use `{}`. Routine status logs, codec diagnostics, and player messages need no forced locator.
- **Upstream:** Preserve wording and line breaks except necessary domain/value and formatting substitutions. Avoid silent fallbacks or style-only helpers.

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
Single-player does not replace a dedicated-server test; NeoForge's server console accepts `stop`.

| Output                  | Path                                |
|-------------------------|-------------------------------------|
| Fabric generated data   | `fabric/src/main/generated/`        |
| NeoForge generated data | `neoforge/src/generated/resources/` |
| JAR files               | `<module>/build/libs/`              |

Inspect generated resources after provider changes.
CI builds, validates access changes, publishes to Maven local, and uploads artifacts on pushes and PRs; distribution is separate.

### Maven Publication

| Command                         | Destination                                   |
|---------------------------------|-----------------------------------------------|
| `./gradlew publishToMavenLocal` | `~/.m2/repository`                            |
| `./gradlew publish`             | Project `repo/` or `local_maven_url` override |

Root tasks publish Common, Fabric, and NeoForge.
Common contains shared main code; loader outputs include applicable shared client code.
Distribution is separate.

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
Open a PR when required, squash temporary commits if appropriate, and delete merged temporary branches.
Direct pushes follow repository rules.

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
| Target behind `dev` without independent commits | Fast-forward   |
| Both branches have independent commits          | Reviewed merge |
| Only selected changes apply                     | Cherry-pick    |

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
# Choose: git merge --ff-only origin/dev  OR  git cherry-pick COMMIT_SHA
git push origin MC_BRANCH
```

For PRs, branch **from the destination branch**.
Review and test each port; cherry-pick does not adapt APIs.
If fast-forward fails, inspect history rather than force-pushing; check CI and resulting commits.

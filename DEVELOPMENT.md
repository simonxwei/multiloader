# Development Guide

Create, build, and maintain a mod with this template.
See [Releasing](RELEASING.md) for versions, tags, and publication.

## Prepare the Workspace

1. Install a JDK matching `java_version` in `gradle.properties`.
2. Import the Gradle project into your IDE; IntelliJ IDEA is recommended.
3. Run the included wrapper from the repository root. On Windows, use `gradlew.bat` in place of `./gradlew`.

## Create a Mod

| Update                                      | Locations                                                   |
|---------------------------------------------|-------------------------------------------------------------|
| Project, versions, identity, links, license | `settings.gradle`, `gradle.properties`, `LICENSE`           |
| Java package and `Template*` classes        | Refactor in the IDE across all three modules                |
| Source identity                             | `TemplateConstants.MOD_ID` and `MOD_NAME`                   |
| Loader entrypoints and resource paths       | `fabric.mod.json`, `neoforge.mods.toml`, Mixin JSON         |
| Service discovery                           | `META-INF/services` filenames **and** provider names        |
| Resources                                   | Rename `template.*` files; replace artwork and example data |
| Documentation                               | Replace README and adapt these guides to the new mod        |

Class names, Mixin packages, and file references are literal for IDE navigation.
Changing Gradle properties does not rename sources or resources.
Edit data providers, regenerate their outputs, and remove obsolete generated examples.

**Check:** Search for old identifiers and broken references, refresh Gradle, then [build and test](#build-and-test) both loaders.
`mod_version=1.0.0` is a starter mod version, independent of the template's release version.

## Make a Change

For isolated work, start from an updated `dev` branch:

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

Replace the example branch and message. Make one coherent change per commit.
Use a PR when review, isolation, or repository rules require it; delete temporary branches after integration.
Direct updates are appropriate only when repository rules permit them.

### Commit Messages

Use lightweight [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```text
<type>(<optional-scope>): <imperative description>
```

| Type                | Use for                          |
|---------------------|----------------------------------|
| `feat` / `fix`      | Functionality / bug fixes        |
| `refactor` / `perf` | Restructuring / performance      |
| `docs` / `test`     | Documentation / tests            |
| `build` / `ci`      | Build configuration / automation |
| `chore`             | Other maintenance                |

```text
refactor: align loader entrypoint names
fix(resources): correct icon path
docs: clarify resource renaming
```

Use a body for non-obvious reasoning and `!` or a `BREAKING CHANGE:` footer for breaking changes.
Commit types do not automatically determine release versions.

### Pull Requests and Issues

| Item       | Include                                                                           |
|------------|-----------------------------------------------------------------------------------|
| PR         | What changed, why, linked issues, checks actually performed                       |
| Bug report | Minecraft and loader versions, reproduction steps, expected/actual behavior, logs |
| Proposal   | Goal and acceptance conditions                                                    |

Review the diff and CI before merging. Squash intermediate commits that form one logical change.
Close resolved issues; routine edits need no separate issue.

## Build and Test

Validate compilation, Fabric access changes, and Maven-local publication:

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

A successful build or single-player session does not replace dedicated-server testing.
After provider changes, run the applicable data-generation tasks and inspect generated resources.
NeoForge's server run accepts console input such as `stop`.

JARs appear in each module's `build/libs/`.
CI builds, validates Fabric access changes, checks Maven-local publication, and uploads artifacts on pushes and PRs.
Release publication is a separate step.

## Synchronize Minecraft Branches

| Target history and compatibility                             | Method         |
|--------------------------------------------------------------|----------------|
| Behind `dev`, no independent commits, all changes compatible | Fast-forward   |
| Independent commits on both branches, changes compatible     | Reviewed merge |
| Only selected changes apply                                  | Cherry-pick    |

Replace `MC_BRANCH` and `COMMIT_SHA` below. Push the source commits before synchronizing.

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
```

Run the chosen operation: `git merge --ff-only origin/dev` for fast-forward, or `git cherry-pick COMMIT_SHA` for selected commits.
Review, build, and test, then run `git push origin MC_BRANCH`.
For a required PR, work on a temporary branch created **from the destination branch**.
Cherry-pick creates new commits and does not adapt APIs automatically.
If fast-forward fails, inspect the histories; do not force-push to make branches match.
Check the resulting commit and CI. See [Releasing](RELEASING.md#maintain-released-versions) for backports.

## Reference: Source Layout

| Location             | Responsibility                                              |
|----------------------|-------------------------------------------------------------|
| `build-logic/`       | Shared Gradle conventions                                   |
| `common/src/main/`   | Loader-independent code for both physical sides             |
| `common/src/client/` | Shared client-only code                                     |
| `fabric/src/main/`   | Fabric common-side integration                              |
| `fabric/src/client/` | Fabric client integration and data generation               |
| `neoforge/src/main/` | NeoForge integration, with Dist-aware client initialization |

Common main must not reference loader APIs or client-only types; Common client must not reference loader APIs.
Fabric separates client sources. NeoForge packages shared client code in its universal JAR and guards client initialization.

### Shared Definitions

Maintain shared definitions once; let each loader adapt them at its own lifecycle point.

| Definition in Common                         | Loader responsibility                |
|----------------------------------------------|--------------------------------------|
| Registry names, entries, and factories       | Registration API and timing          |
| Payload types, codecs, and registration list | Registration and receiver setup      |
| Data-generation bootstrap and shared data    | Provider setup and output            |
| Shared translation entries                   | Output and platform-specific entries |

Illustrative translation pattern; `TemplateTranslations` is not a bundled class:

```java
// Common: TemplateTranslations.addTranslations(BiConsumer<String, String> add)
public static void addTranslations(final BiConsumer<String, String> add) {
    add.accept("template.example", "Example");
}

// Fabric: inside generateTranslations(...).
TemplateTranslations.addTranslations(translationBuilder::add);
// NeoForge: inside addTranslations().
TemplateTranslations.addTranslations(this::add);
```

Use a small callback for shared declaration lists and `TemplateServices` for platform implementations called by Common.
Keep platform-specific entries, lifecycle ordering, and execution-side rules in the platform modules.
Sharing a list does not make the loaders' APIs or timing interchangeable.

### Configuration and Resources

| Properties                                | Purpose                                      |
|-------------------------------------------|----------------------------------------------|
| `minecraft_*`, `java_version`, `username` | Game target and development environment      |
| `fabric_*`, `neoforge_*`                  | Loader, tooling, and compatibility           |
| `mixin_*`                                 | Mixin and MixinExtras requirements           |
| `mod_*`                                   | Identity, artifact coordinates, and metadata |

Keep dependency versions in Gradle configuration, not reusable comments.
NeoForge's ModDevGradle, NeoForm, NeoForge, and JavaFML properties describe separate layers.

| Literal references                                                               | Expanded values                                                              |
|----------------------------------------------------------------------------------|------------------------------------------------------------------------------|
| Entrypoint classes, Mixin packages, Mixin/class-tweaker filenames, artwork paths | Mod metadata, dependency versions, Mixin minimum version, Java compatibility |

Keep Fabric class-tweaker and NeoForge Access Transformer entries semantically equivalent for shared access changes.
Use `.gitkeep` for intentional empty directories; remove it when real files arrive.
The build excludes these placeholders from binary/source JARs and Javadoc.

## Reference: Code Conventions

### Naming

| Case                                     | Pattern / example                                              |
|------------------------------------------|----------------------------------------------------------------|
| Common initialization                    | `TemplateModCommon`, `TemplateModCommonClient`                 |
| Loader entrypoint, in each loader module | `TemplateMod`, `TemplateModClient`, `TemplateModDataGenerator` |
| Vanilla counterpart                      | Prefix the full name: `TemplateRandomState`                    |
| True domain counterpart                  | Replace the domain term; document that mod's mapping           |
| Parallel module implementation           | Keep the full vanilla name: `TemplateCommonRandomState`        |
| Internal helper                          | Omit redundant prefixes when the package and role are clear    |
| Mixin                                    | Target name + `Mixin`                                          |
| Injected member / bridge                 | `template$cachedValue`; use `@Unique` as needed                |
| Ordinary member                          | Standard camelCase                                             |

Identical loader entrypoint names belong to separate module outputs.
`@Shadow` names follow their mapped targets. Use the full mod ID for injected-member collision protection.
Do not add wrappers or duplicate behavior just for naming symmetry.

### Comments and Javadoc

| Purpose                    | Example / rule                                                         |
|----------------------------|------------------------------------------------------------------------|
| Customization point        | `// Register Fabric data providers here.`                              |
| Non-obvious constraint     | `// Load platform implementations without loader APIs in common code.` |
| Clear upstream counterpart | `@see RandomState`; add `@author` where applicable                     |
| Method navigation          | `{@link TemplateConstants#id(String)}`                                 |
| File syntax                | Java/Groovy: `//`; YAML/properties: `#`                                |
| Properties                 | `#` section heading; `##` property note                                |

Explain intent, constraints, and replacement points; omit code narration and routine Javadoc on helpers or Mixins.
Use brief English sentences ending in periods. Start each sentence on its own line; wrap at natural boundaries.
Preserve useful upstream URLs without trailing punctuation. Keep instructions out of JSON, service descriptors, and generated files.

### Exceptions and Diagnostic Logs

The following are message-format examples; placeholder members need not exist in the template.

| Reference                | Example message                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Parameter / local        | `"serviceClass"` or `"version: expected=" + VERSION + ", actual=" + version` |
| Member field             | `"TemplateServices#PLATFORM: not initialized"`                               |
| Method                   | `"TemplateServices#load(): cannot load service"`                             |
| Shadow member            | `"TargetClass#method(): mixin shadow method is unavailable"`                 |
| Injected member          | `"TargetMixin#template$cachedValue: not initialized"`                        |
| Code name in explanation | `"provider: 'TemplatePlatformHelper' implementation required"`               |
| Nested type              | `"TemplateProvider.Entry#load(): entry is unavailable"`                      |

- **Reference:** use bare parameter/local names or the declaring type, including enclosing types. Shadows name the original declaration; injected members name the Mixin. `Type#method()` is a plain-text locator: omit parameter types and clarify overloads only when needed.
- **Message:** `Objects.requireNonNull` normally needs only the name. Add `: explanation` when useful; other exceptions need a clear reason. Start explanations lowercase, preserving code spelling; single-quote code names in explanations, not prefixes. Omit trailing periods and redundant `Error:` prefixes. Logs use `{}`; routine status logs need no locator.
- **Behavior:** use `IllegalArgumentException` for invalid inputs, `IllegalStateException` for invalid state, and `UnsupportedOperationException` for unsupported operations. Preserve checks, causes, log levels, and crash reports; do not hide violations with fallbacks or add formatting helpers for style alone.
- **Upstream:** preserve wording and line breaks except necessary domain/value substitutions and the above reference, capitalization, quoting, and punctuation adjustments. Codec diagnostics and player messages need no forced locator.

### Modifiers

Use `final` for method/constructor parameters and concrete classes unless inheritance is intentional.
Local-variable `final` is optional. Use minimal visibility and private constructors for static utility classes.

**IDE:** Verify actual Gradle failures before changing valid Groovy DSL calls to satisfy inspections; avoid global suppression.

## Maven Publication

| Command                         | Destination                                                                    |
|---------------------------------|--------------------------------------------------------------------------------|
| `./gradlew publishToMavenLocal` | Local Maven repository, normally `~/.m2/repository`                            |
| `./gradlew publish`             | Project-root `repo/`, overridden by the `local_maven_url` environment variable |

Root publication tasks include Common, Fabric, and NeoForge.
The Common publication contains shared main code; loader outputs also include the applicable shared client code.
GitHub Releases and mod distribution sites require separate publication steps.

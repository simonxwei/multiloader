# Development Guide

Use this guide to create a mod, make changes, verify builds, and maintain Minecraft branches.

See [Releasing](RELEASING.md) for versions, tags, and publication.

---

## Prepare the Workspace

1. Install a JDK matching `java_version` in `gradle.properties`.
2. Open the repository in a Java IDE (IntelliJ IDEA is recommended) and import the Gradle project.
3. Use the included Gradle wrapper; a separate Gradle installation is unnecessary.

Run Gradle commands from the repository root.
On Windows, use `gradlew.bat` in Command Prompt or PowerShell instead of `./gradlew` where appropriate.

## Create a Mod

Work through this checklist when copying the template into a new project:

1. Rename `rootProject.name` in `settings.gradle`. Set the Minecraft, Java, loader, Mixin, and `mod_*` properties in `gradle.properties`. Replace the example repository links and license as needed.
2. Use the IDE to refactor the Java package and `Template*` classes, including imports, client entrypoints, and data generators. Changing `mod_package` alone does **not** rename Java sources.
3. Update `TemplateConstants.MOD_ID` and `MOD_NAME` to match `gradle.properties`. Keep the mod ID, Java package, and source-level project prefix distinct.
4. Rename `META-INF/services` descriptor **filenames and provider names**. Update Mixin JSON filenames and `package` fields, the Fabric class-tweaker filename, and all corresponding loader-metadata references.
5. Replace template code, icons, banner, and README. Edit data providers rather than generated JSON; regenerate data and remove obsolete examples. Keep only useful `.gitkeep` placeholders.
6. Refresh Gradle and complete the [build and runtime checks](#build-and-test) for both loaders.

`mod_version=1.0.0` is a **starter example for a mod**, not the multiloader's release version.
Set an appropriate version for the new project; see [Releasing](RELEASING.md#choose-a-version-and-target).

**Check:** Search for obsolete package names, `Template*` identifiers, old mod IDs,
and mismatched metadata references before the first commit.

## Make a Change

Use this sequence for ordinary development:

1. Update your working branch (`git pull --ff-only`) and decide whether the change needs a temporary branch.
2. Make one coherent change, including any related tests or resources.
3. Run the relevant [checks](#build-and-test); inspect generated files and `git diff`.
4. Commit the change using the [commit format](#commit-messages), then push it.
5. Use a pull request (PR) when review, isolation, or branch rules call for one. Check the diff and CI status before merging.

For isolated work, create a branch from `dev`:

```shell
git switch dev
git pull --ff-only origin dev
git switch -c feature/example-change
# Edit and verify the files.
git status
git diff --check
git add -A
git diff --cached --stat
git commit -m "feat: describe the change"
git push -u origin feature/example-change
```

Replace the sample branch and commit message.
For small changes, a temporary branch or PR is optional **only when repository rules permit direct updates**.
Delete temporary branches after integration.

### Commit Messages

Use lightweight [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```text
<type>(<optional-scope>): <imperative description>
```

| Type                | Use for                                       |
|---------------------|-----------------------------------------------|
| `feat` / `fix`      | New functionality / bug fixes                 |
| `refactor` / `perf` | Internal restructuring / performance          |
| `docs` / `test`     | Documentation / tests                         |
| `build` / `ci`      | Dependencies or Gradle / automation workflows |
| `chore`             | Other maintenance                             |

Examples: `feat(worldgen): add cliff texture operator`, `fix: correct material rule precedence`,
and `docs: clarify release versioning`.

Make each commit independently understandable, even when it changes several files.
Do not mix unrelated work to reduce commit count.
A scope is optional; use a body for non-obvious reasoning.
Mark breaking changes with `!` or a `BREAKING CHANGE:` footer where relevant.
Commit types do **not** automatically determine release versions.

### Pull Requests and Issues

**PR:** Open one when a change needs review, an isolated CI run, discussion, or approval under branch rules.
Use a descriptive title (preferably the commit format) and explain **what** changed and **why**.
Link relevant issues and state the checks actually performed.
Review the diff and checks before merging.
Use **Squash and merge** when several intermediate commits form one logical change.

**Issue:** Create one for bugs, features, or investigations worth tracking; routine edits need none.
For bugs, include the Minecraft version, loader, reproduction steps, expected/actual behavior,
and relevant logs.
For proposals, describe the goal and acceptance conditions.
Use labels and comments to track findings; link PRs and close the issue when resolved or declined.

## Build and Test

From the repository root, validate compilation, Fabric access changes, and Maven-local publication:

```shell
./gradlew clean build :fabric:validateAccessWidener publishToMavenLocal --warning-mode all
```

Run the applicable game environments **one at a time**, stopping each cleanly:

```shell
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew :fabric:runServer
./gradlew :neoforge:runServer
```

**Check:** Confirm normal startup and shutdown on both physical clients and dedicated servers.
A passing build or single-player session does not replace dedicated-server testing.

When adding data providers, run the relevant loader's data-generation task and review generated resources before committing.
Remove stale examples.
NeoForge run configurations can forward console input such as `stop`.

Find outputs under each project's `build/libs/`.
GitHub Actions builds on pushes and PRs, validates Fabric access changes,
checks Maven-local publication, and uploads CI artifacts.
It does **not** publish GitHub Releases or upload to Modrinth/CurseForge.

## Synchronize Minecraft Branches

Each `<minecraft_version>` branch maintains its own compatible code.
Choose the method **before** synchronizing:

| Condition                                                                           | Method             |
|-------------------------------------------------------------------------------------|--------------------|
| The target is behind `dev`, with no independent commits; all changes are compatible | Fast-forward merge |
| Both branches have independent commits and should combine their histories           | Reviewed merge     |
| Only selected changes apply to an older/different Minecraft version                 | Cherry-pick        |

For the first case, push the updates to `dev`, then replace `MC_BRANCH` with the actual branch name:

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
git merge --ff-only origin/dev
git push origin MC_BRANCH
```

**Check:** Confirm the target branch now points to the expected commit, and that CI passes.

For a documentation fix or other selected change, apply only the appropriate commit to the destination branch.
Replace `MC_BRANCH` and `COMMIT_SHA` before running these commands:

```shell
git fetch origin
git switch MC_BRANCH
git pull --ff-only origin MC_BRANCH
git cherry-pick COMMIT_SHA
git push origin MC_BRANCH
```

Cherry-pick creates a new commit on the target branch.
If a PR is required, create a temporary branch **from the destination branch**,
cherry-pick there, and open the PR back into that destination.
A PR from `dev` into an older Minecraft branch can also merge unwanted Gradle, API, and dependency changes.

**Do not** blindly merge APIs or loader dependencies for a newer Minecraft release into an older branch.
If `--ff-only` fails, inspect the histories rather than force-pushing.
Follow required PR checks and branch protection.
See [Releasing](RELEASING.md#maintain-released-versions) for backports and tags.

## Reference: Source Layout

| Location             | Responsibility                                            |
|----------------------|-----------------------------------------------------------|
| `build-logic/`       | Shared Gradle conventions                                 |
| `common/src/main/`   | Loader-independent code usable on both physical sides     |
| `common/src/client/` | Shared client-only code, without loader APIs              |
| `fabric/src/main/`   | Fabric common-side integration                            |
| `fabric/src/client/` | Fabric client code and data generation                    |
| `neoforge/src/main/` | NeoForge integration and Dist-aware client initialization |

Never reference loader APIs or client-only Minecraft types from `common/src/main/`.
Fabric separates client sources; NeoForge includes shared client code in its universal output
and guards client initialization.
Keep platform-specific behavior in platform modules or service implementations.

### Configuration and Resources

| Properties                                | Purpose                                          |
|-------------------------------------------|--------------------------------------------------|
| `minecraft_*`, `java_version`, `username` | Game target and development environment          |
| `fabric_*`, `neoforge_*`                  | Loader, tooling, dependencies, and compatibility |
| `mixin_*`                                 | Mixin and MixinExtras requirements               |
| `mod_*`                                   | Identity, artifact coordinates, and metadata     |

Keep actual Minecraft and dependency versions in the branch's Gradle configuration, not in reusable comments or rules.
NeoForge's `neoforge_moddev_version`, `neoforge_neoform_version`, `neoforge_version`,
and `neoforge_javafml_version_range` refer to distinct tooling, development artifact,
platform, and language-loader layers.

Gradle expands selected `${mod_id}` and `${mod_package}` values in resources, but does not rename Java types.
Keep Fabric class-tweaker and NeoForge Access Transformer changes semantically equivalent
when shared code needs the same access.

Git cannot track empty directories.
Use `.gitkeep` only for intentional empty locations; remove it when real files arrive.
The template reserves Mixin paths under `common/src/main`, `common/src/client`,
`fabric/src/main`, `fabric/src/client`, and `neoforge/src/main`.
The shared Gradle rules exclude `**/.gitkeep` from JARs (including sources JARs) and Javadoc.
Never list it as a Mixin or place it in a resource root without checking packaging.

## Reference: Code Conventions

These are maintainability defaults.
Do not add wrappers or duplicate vanilla behavior merely to satisfy naming symmetry.

### Naming

| Case                                      | Rule                                                                                                                               |
|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| Vanilla counterpart                       | Prefix the **full** vanilla type name: `<ProjectPrefix>RandomState`                                                                |
| True domain counterpart                   | Replace the corresponding domain term without duplicating the project prefix; document the mapping in that mod                     |
| Common / loader entrypoint                | `<ProjectPrefix><Module>Mod`, with `Common`, `Fabric`, or `NeoForge`; client and data-generation classes follow the same qualifier |
| Parallel module implementation            | Keep the full vanilla name after the module qualifier: `<ProjectPrefix>CommonRandomState`                                          |
| Internal helper                           | Avoid a redundant prefix when the package and role are already clear                                                               |
| Mixin type                                | Vanilla target name followed by `Mixin`                                                                                            |
| New injected Mixin member / bridge method | Full mod ID + `$` (for example, `modid$cachedValue`); use `@Unique` as needed                                                      |
| Ordinary Java member                      | Standard camelCase, without the Mixin `$` convention                                                                               |

The Common initialization class holds shared logic.
Fabric and NeoForge classes are loader entrypoints.
`@Shadow` members must follow their actual mapped targets.
Injected Mixin names need collision protection because multiple mods can modify the same target class.

### Comments and Javadoc

- **Explain what matters:** template replacement points, non-obvious intent, loader or client-side constraints, and upstream references. Remove comments that merely restate code.
- **Keep comments brief:** use English sentence case and preferably one sentence per comment, ending with a period. Put each distinct sentence on a separate source line. Wrap long sentences at natural clause boundaries without splitting words, identifiers, links, or code spans. Place comments immediately above the relevant code.
- **Use actionable wording:** write short imperatives at template customization points(for example, `// Rename this project when creating a mod.`). Keep full procedures in this guide.
- **Follow file syntax:** use `//` for Java/Groovy and `#` for YAML and properties comments. In `gradle.properties`, use `#` for section headings and `##` for per-property notes. Preserve useful upstream URLs; do not append punctuation to URL-only lines.
- **Avoid generated content:** do not add instructional comments to JSON, ServiceLoader descriptors, or generated resources. Update or remove stale comments when code changes.
- **Keep Javadoc navigational:** use `@see` and `@author` for clear vanilla counterparts, and `{@link ...}` for corresponding methods where useful. Link to actual mapped members; omit routine Javadoc on unrelated helpers and Mixin types.

### Exceptions and Modifiers

- Choose `IllegalArgumentException` for bad inputs, `IllegalStateException` for invalid state, and `UnsupportedOperationException` only for unsupported operations.
- Exception messages use concise English sentence case, with no trailing period or redundant `Error:` prefix. Include actual values when helpful; do not hide invariant violations with fallbacks.
- Declare method and constructor parameters `final` by default, and concrete classes `final` unless inheritance is intentional. Local-variable `final` is optional. Use minimal required visibility; static utility classes normally have private constructors.

```java
throw new IllegalArgumentException("Material rule sequence must not be empty");
throw new IllegalStateException("Expected CustomState, got " + state.getClass().getName());
Objects.requireNonNull(source, "source");
```

**IDE note:** Dynamic Gradle/Groovy DSL methods may trigger IntelliJ inspections even when builds succeed.
Verify actual Gradle errors before changing scripts.
Do not suppress inspections globally or rename valid APIs solely to silence them.

## Maven Publication

`publishToMavenLocal` writes to the user's local Maven repository (normally `~/.m2/repository`).
The root `publish` task publishes `common`, Fabric, and NeoForge to the project-root `repo/` by default.
`local_maven_url` can override that destination.
The published `common` component contains shared main sources; loader builds incorporate the applicable shared client sources.
Neither command automatically creates a GitHub Release or publishes to a mod distribution service.
See [Releasing](RELEASING.md) for release steps.

# Releasing

This file covers two separate workflows:

1. releasing the **multiloader template repository**;
2. releasing a **mod created from the template**.

They use different version numbers. A generated mod should adapt the second workflow to its own release and support policy.

---

## Version Terms

| Term | Meaning |
|---|---|
| `minecraft_version` | Minecraft version targeted by the current branch |
| Template version | Version assigned to a template Git tag and GitHub Release |
| `mod_version` | Version of the mod built from the template |

A template release does not require changing `mod_version`.
The template version may exist only in the release tag and title, while `mod_version` remains a starter value for generated projects.

# Template Repository Releases

## Naming

| Item | Convention |
|---|---|
| Primary development branch | `dev` |
| Minecraft version branch | `<minecraft_version>` |
| Release tag | `mc<minecraft_version>-<template_version>` |
| GitHub release title | `multiloader <template_version> for Minecraft <minecraft_version>` |

One tag records the tested common, Fabric, and NeoForge template state from the same commit.
Template releases may use stable versions directly; prerelease suffixes are only needed for intentionally experimental public builds.

## Verify

Run the combined build, access validation, publication smoke test, and Gradle deprecation check from the repository root:

```shell
./gradlew clean build :fabric:validateAccessWidener publishToMavenLocal --warning-mode all
```

Then verify all four runtime environments in sequence:

```shell
./gradlew :fabric:runClient :neoforge:runClient :fabric:runServer :neoforge:runServer
```

Close each client normally so Gradle can continue to the next run.
Dedicated servers should reach the `Done` message and be stopped cleanly with `stop`.
The build also validates NeoForge Access Transformer targets.
`publishToMavenLocal` is a publication smoke test, not a remote release.

## Publish a Template for a Minecraft Version

1. Complete and verify the intended commit on `dev`.
2. Create or update the branch named with the target `minecraft_version`.
3. Push the tested version branch.
4. Choose a template version independently of `mod_version`.
5. Create `mc<minecraft_version>-<template_version>` from the tested commit.
6. Create a GitHub Release titled `multiloader <template_version> for Minecraft <minecraft_version>`.

A concise release description is sufficient:

```markdown
Minecraft <minecraft_version> multiloader template for Fabric and NeoForge.

Tested on both loaders and on dedicated servers.
```

## Update a Template Version Branch

A released Minecraft branch may receive useful fixes or dependency updates.

1. Apply only changes appropriate for that Minecraft version.
2. Keep `mod_version` unchanged unless the generated-mod default itself should change.
3. Repeat the verification checks.
4. Choose a new template version and create a new immutable tag.
5. Apply the change to `dev` separately when it remains relevant there.

While `dev` targets the same Minecraft version, merging tested `dev` changes into the version branch is reasonable.
After `dev` advances, backport or cherry-pick relevant fixes instead of merging unrelated next-version work.

A published branch records a usable template state but does not promise continuous maintenance.

# Mod Releases After Using the Template

This section is a starting policy for an actual mod project.
Replace template-specific wording and adjust the workflow to match the project's own release and support plans.

## Naming

| Item | Suggested convention |
|---|---|
| Primary development branch | `dev` |
| Minecraft version branch | `<minecraft_version>` |
| Release tag | `mc<minecraft_version>-<mod_version>` |
| GitHub release title | `<mod_name> <mod_version> for Minecraft <minecraft_version>` |

Use `minecraft_version` and `mod_version` from `gradle.properties`.
One tag represents the common, Fabric, and NeoForge outputs together.

Prerelease versions may use Semantic Versioning suffixes:

```text
<major>.<minor>.<patch>-alpha.<number>
<major>.<minor>.<patch>-beta.<number>
<major>.<minor>.<patch>-rc.<number>
<major>.<minor>.<patch>
```

Mark alpha, beta, and release-candidate builds as prereleases on GitHub.
Stable releases omit the prerelease suffix.

## Verify

Use the same build, publication, and runtime checks listed for the template.
Both loaders should be tested before creating a shared release tag.
Add project-specific migration, compatibility, networking, or saved-data checks when relevant.

## Publish a Mod for a Minecraft Version

1. Complete and verify the release candidate on `dev`.
2. Create or update the branch named with the target `minecraft_version`.
3. Set the intended `mod_version` and commit the release state.
4. Push the tested version branch.
5. Create `mc<minecraft_version>-<mod_version>` from that commit.
6. Create a GitHub Release titled `<mod_name> <mod_version> for Minecraft <minecraft_version>`.
7. Mark prerelease versions appropriately; publish stable versions without the prerelease flag.

A concise prerelease description may be:

```markdown
<mod_name> <mod_version> for Minecraft <minecraft_version>.

This is an early development release intended for testing and feedback.
Back up important worlds before use when the mod changes world generation or saved data.

Tested on both loaders.
```

A concise stable description may be:

```markdown
<mod_name> <mod_version> for Minecraft <minecraft_version>.

Tested on both loaders.
```

## Update a Released Mod Version

Maintenance of a released Minecraft version is a project decision.
When an update is warranted:

1. Check out the corresponding Minecraft version branch.
2. Increase `mod_version`.
3. Apply only relevant fixes or compatible improvements.
4. Verify both loaders and push the branch.
5. Create a new immutable tag.
6. Apply the fix to `dev` separately when it remains relevant there.

Do not merge unrelated next-version work into an older branch.
Existing release tags must never be moved or reused.

## Suggested Support Principles

- `dev` receives new features and architecture changes.
- Version branches record released states and may move at a slower maintenance cadence.
- Publishing a Minecraft version does not promise indefinite support.
- Backports should prioritize crashes, corruption, saved-data failures, and major compatibility problems.
- New features and large refactors do not need to be backported.
- Support decisions may follow ecosystem adoption and available maintenance time.

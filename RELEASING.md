# Releasing

This document describes a reusable release workflow for a multiloader Minecraft project.
The template repository and a mod created from it can follow the same verification process, but their **release versions are independent**.
Adapt branch names, release channels, and support commitments to the project.

Use `<minecraft_version>`, `<project_version>`, and `<project_name>` as explanatory placeholders, not literal names.
Read actual Minecraft and mod versions from `gradle.properties`.

---

## Verification

Build and validate access changes from a clean checkout:

```shell
./gradlew clean build :fabric:validateAccessWidener --warning-mode all
```

If Maven publication is configured, separately smoke-test its metadata and artifacts:

```shell
./gradlew publishToMavenLocal --warning-mode all
```

`publishToMavenLocal` writes to Maven-local; `publish` uses the repository target configured in the build. Neither command publishes a GitHub Release.

Run each environment separately, including both dedicated servers:

```shell
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew :fabric:runServer
./gradlew :neoforge:runServer
```

Confirm that clients reach the normal game state and dedicated servers start and stop cleanly.
A successful build does **not** establish runtime compatibility.
Add world loading, persistence, data generation, networking, and migration checks as appropriate to the project.
Only report verification actually performed.

Before publishing, inspect the generated artifacts, resources, version metadata, LICENSE, release notes, and any compatibility limitations.
When changing persistent world data, include backup and migration guidance.

## Versioning and Git Branches

| Item                     | Suggested convention                                                 |
|--------------------------|----------------------------------------------------------------------|
| Development branch       | `dev`                                                                |
| Minecraft-version branch | `<minecraft_version>`                                                |
| Release tag              | `mc<minecraft_version>-<project_version>`                            |
| Release title            | `<project_name> <project_version> for Minecraft <minecraft_version>` |

The **template release version** is a tag or release of the template repository; it must not be confused with the starter `mod_version` property intended for mods copied from the template.
A derived mod instead uses its own `mod_version` for published artifacts.
Choose a consistent prerelease format such as `-alpha.1`, `-beta.1`, or `-rc.1` when needed.

Use immutable published tags.
Releasing a new Minecraft-version branch may begin a new project-version sequence if that is the repository's policy; do not assume all branches must share one global sequence.

## Release Procedure

1. Finish and review changes on the development branch, or on an isolated test branch to be merged into it.
2. Run the build and all relevant runtime checks on the intended release candidate.
3. Synchronize the corresponding Minecraft-version branch with the **tested commit**, resolving any conflicts before release.
4. Set the project's version and inspect metadata and build artifacts; commit any changes and revalidate the final state.
5. Push the tested release commit, create a **new** tag, and publish the release with an accurate change summary.
6. Distribute both loader artifacts through the project's chosen channels, if applicable.

Example release note:

```text
<project_name> <project_version> for Minecraft <minecraft_version>

Changes: <key changes>
Verified: <loaders and environments actually tested>
Known limitations: <if any>
```

### Updating Through a Temporary Branch

When importing a new template ZIP into an existing repository, use a short-lived branch.
The following shell commands assume `dev` is clean, `test` does not already exist, and both branches target the **same Minecraft release**.
Use Git Bash or another POSIX-style shell on Windows; PowerShell variable assignment syntax differs.

```shell
git fetch origin
git switch dev
git pull --ff-only origin dev
git switch -c test
```

Copy the **contents of the ZIP's project directory** into the working tree without deleting `.git/`.
Remove old tracked files that were intentionally renamed or removed, then inspect and commit the result:

```shell
git status
git diff --check
git add -A
git diff --cached --stat
git commit -m "Refine multiloader template"
git push -u origin test
```

Once the test branch passes verification, integrate it through the repository's normal review process:

1. Open a pull request from `test` into `dev` and review the complete diff and required checks.
2. After the merge, open a pull request from `dev` into the matching Minecraft-version branch.
3. Confirm that `dev` contains only changes appropriate for that version, then complete the required reviews and checks.

If the target branches are not protected and direct pushes are permitted, equivalent local merges are possible.
Protected branches may require pull requests, so do not assume `git merge` followed by `git push` is allowed.
When branches diverge, inspect the commit graph before choosing a merge or selective `cherry-pick`.
Do not force-push merely to make branches match.

After confirming the integration:

```shell
git switch dev
git branch -d test
git push origin --delete test
```

Use a distinct test branch name if `test` is already in use.
Importing a ZIP or applying a patch is **not** a Git rebase.

### Maintaining Other Minecraft Versions

- For an **older** Minecraft branch, backport only compatible fixes, using `cherry-pick` or a carefully reviewed merge. Never blindly merge next-version API or dependency changes.
- Repeat build/runtime tests for the affected version and create a new tag from its tested commit. Never move a published tag.
- Treat backport support as a project decision; prioritize crashes, world-data corruption, and significant regressions.
- Use `rebase` for intentionally replaying local commits onto a new base, not for rewriting published release-branch history.

See [DEVELOPMENT.md](DEVELOPMENT.md) for setup, source boundaries, code conventions, and build commands.

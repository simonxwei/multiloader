# Release Guide

Use this checklist to release either the Multiloader template or a mod created from it.
**Template release versions and derived-mod versions are independent.**

See [Development](DEVELOPMENT.md) for daily commits, PRs, and branch synchronization.

---

## Choose a Version and Target

1. Select the compatible Minecraft branch (`<minecraft_version>`) and the tested commit to release.
2. Choose a new project version (`MAJOR.MINOR.PATCH`, with an optional prerelease suffix).
3. Confirm the tag and Release title before creating the draft.

| Item             | Format                                                               |
|------------------|----------------------------------------------------------------------|
| Minecraft branch | `<minecraft_version>`                                                |
| Git tag          | `mc<minecraft_version>-<project_version>`                            |
| Release title    | `<project_name> <project_version> for Minecraft <minecraft_version>` |

The Minecraft prefix identifies compatibility; **do not reset the project's version sequence** when Minecraft changes.
The same project version may target multiple Minecraft versions when the feature baseline is equivalent; implementation, dependencies, and Gradle configuration can differ.
Older maintained branches may remain on an earlier version series and receive their own updates.
For example, `mc26.2-0.1.4` may be released after `mc26.3-0.2.0` as a fix to the older feature series.

Use `-alpha.1` for early tests, `-beta.1` for wider tests, or `-rc.1` for release candidates when needed.
These suffixes are optional even in `0.x.x`; mark such GitHub Releases as **Pre-release**.
See [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

**Template:** Multiloader starts at `0.1.0` and intentionally remains in `0.x.x`.
Use Minor for meaningful capabilities or structural changes and Patch for smaller fixes.
Its policy does not restrict derived mods from reaching `1.0.0`.

**Derived mod:** Choose its own version and set `mod_version` in `gradle.properties`.
The template's `mod_version=1.0.0` is only a starter example; it is **not** the template's Release version.

## Verify the Release Candidate

1. Update the intended version or metadata, if needed, and commit the changes.
2. Validate the candidate from a clean checkout:

   ```shell
   ./gradlew clean build :fabric:validateAccessWidener --warning-mode all
   ```

3. Run Fabric and NeoForge clients and dedicated servers **separately**, stopping each cleanly:

   ```shell
   ./gradlew :fabric:runClient
   ./gradlew :neoforge:runClient
   ./gradlew :fabric:runServer
   ./gradlew :neoforge:runServer
   ```

4. Run relevant data-generation, persistence, networking, and migration tests. Inspect the output JARs, generated resources, license, version metadata, and release notes.
5. If Maven publication applies, check metadata and artifacts with `./gradlew publishToMavenLocal --warning-mode all`.

**Check:** A successful build is not proof of runtime compatibility.
Document only tests actually run.
If world data may change, provide backup or migration instructions.

## Check Generated Artifacts

Gradle names JARs from the project configuration, not from the Git tag.
Its shared conventions use `${mod_version}+mc${minecraft_version}` for the build version and `${mod_id}-${project.name}` for the archive name:

```text
<mod_id>-fabric-<mod_version>+mc<minecraft_version>.jar
<mod_id>-neoforge-<mod_version>+mc<minecraft_version>.jar
```

Put prerelease labels in `mod_version`, before `+mc...`.
Check the actual files in each module's `build/libs/`, because loaders may add classifiers.
Do not distribute `-dev`, `-sources`, or `-javadoc` JARs as the normal player download.

The template's own release tag may therefore differ from the example mod version embedded in its demonstration JARs.
GitHub-generated **Source code (zip/tar.gz)** assets are source snapshots, not compiled mod JARs.

## Publish on GitHub

1. Push the **verified commit** to its corresponding Minecraft branch. If necessary, synchronize compatible changes from `dev` first; follow the [branch guide](DEVELOPMENT.md#synchronize-minecraft-branches).
2. Open **Releases → Draft a new release** on GitHub. Select a **new tag name** and the exact target commit or branch.
3. Enter the Release title and notes. For a derived mod, attach the distributable Fabric and NeoForge JARs. The template can release a source snapshot without player-installable JARs.
4. Recheck the tag, target commit, Minecraft compatibility, prerelease flag, notes, and **every attached file**. Publish only after all checks pass.
5. Check the published Release and any distribution sites separately. GitHub does not automatically upload to Modrinth or CurseForge unless an explicit automation is configured.

A concise note format:

```text
<project_name> <project_version> for Minecraft <minecraft_version>

Changes: <key changes>
Verified: <tests actually performed>
Known limitations: <if any>
```

### Before Enabling Immutable Releases

GitHub [Immutable Releases](https://docs.github.com/en/code-security/concepts/supply-chain-security/immutable-releases) can protect a published tag, attached artifacts, and release provenance.
**It is optional; ordinary tag naming does not require it.**

- The setting applies to future published Releases, not retroactively.
- Turning it off does **not** unlock Releases published while it was enabled.
- A tag name used by an immutable Release cannot be reused even after deleting that Release and tag, or recreating the repository under the same name.
- Once published immutably, the associated tag and assets cannot be replaced; titles and notes remain editable.

**Default for this template:** leave Immutable Releases disabled while the publishing process is still evolving.
Use drafts to verify everything before publishing.
Correct a published version with a **new version and tag**, not by moving an existing tag.

## Maintain Released Versions

- Keep published tags stable and create a new tag for fixes.
- Merge `dev` into the corresponding Minecraft branch only when the history and all changes are compatible. Use fast-forward for linear histories; choose reviewed merges or selective `cherry-pick` when histories differ.
- Backport selected commits or reimplement their intent when Minecraft APIs differ; `cherry-pick` does not adapt code automatically. Do not merge an entire newer branch just to transfer a doc or bug fix.
- Give two Minecraft builds the same project version only when their advertised feature baseline is equivalent. Older branches may retain an earlier feature series; verify and test each port independently.
- Do not force-push protected or published branch history to make branches match. Respect branch rules requiring PRs and checks.

**Other GitHub cautions:** Closing a PR retains its history.
Changing a public repository to private does not make existing public forks private.
Deleting repositories or rewriting history does not reliably erase existing clones, forks, or references.
Never commit secrets; revoke and rotate exposed credentials.

For routine branches, commits, PRs, and Issues, use [Development](DEVELOPMENT.md#make-a-change).

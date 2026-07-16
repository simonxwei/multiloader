# Releasing

**This repository uses a simple branch and tag layout inspired by [Sodium](https://github.com/CaffeineMC/sodium).** The template publishes only directly usable releases; it does not use alpha, beta, or release-candidate versions.

---

## Names

| Item                      | Format                                                        | Example                                |
|---------------------------|---------------------------------------------------------------|----------------------------------------|
| Latest development branch | `dev`                                                         | `dev`                                  |
| Minecraft version branch  | `<minecraft_version>`                                         | `26.2`                                 |
| Release tag               | `mc<minecraft_version>-<mod_version>`                         | `mc26.2-1.0.0`                         |
| GitHub release title      | `multiloader <mod_version> for Minecraft <minecraft_version>` | `multiloader 1.0.0 for Minecraft 26.2` |

`minecraft_version` and `mod_version` remain separate properties in `gradle.properties`. A Minecraft port does not automatically require a new mod version, while a template change can increase `mod_version` without changing Minecraft compatibility.

One Git tag represents the common, Fabric, and NeoForge outputs together. If an external publishing platform requires loader-specific version identifiers, append the loader only there:

```text
mc26.2-1.0.0-fabric
mc26.2-1.0.0-neoforge
```

These are platform version identifiers, not separate Git tags.

## Verify the release commit

Before creating a Minecraft version branch, run from the repository root:

```shell
./gradlew clean build --warning-mode all
./gradlew :fabric:validateAccessWidener --warning-mode all
./gradlew publishToMavenLocal
```

The build validates NeoForge Access Transformer targets because `validateAccessTransformers` is enabled in both `common` and `neoforge`. `publishToMavenLocal` is a publication smoke test; it writes to the current user's Maven local repository and does not create GitHub release assets.

## Create a Minecraft version branch

Before releasing, make sure the tested commit is pushed to `dev`.

In IntelliJ IDEA:

1. Check out `dev` and use **Git → Push** to ensure `origin/dev` is current.
2. Open the branch menu and choose **New Branch from 'dev'**.
3. Enter the Minecraft version, such as `26.2`, and keep **Checkout branch** enabled.
4. Use **Git → Push** to create and track `origin/26.2`.
5. Switch back to `dev` after the version branch has been published.

The new version branch and `dev` initially point to the same tested commit. Future work for another Minecraft version continues on `dev`; the `26.2` branch records the template released for Minecraft 26.2.

## Create the GitHub Release

On GitHub:

1. Open **Releases → Draft a new release**.
2. Choose **Create new tag** and enter `mc26.2-1.0.0`.
3. Set the target branch to `26.2`.
4. Use the title `multiloader 1.0.0 for Minecraft 26.2`.
5. Do not mark the release as a prerelease.
6. Publish the release after confirming the selected branch and tag names.

A concise description is sufficient:

```markdown
Minecraft 26.2 multiloader template for Fabric and NeoForge.

Tested on both loaders.
```

The version branch may move if a meaningful template fix is later published, but an existing release tag must never be moved or reused.

## Update an existing Minecraft version

Updating an older version branch is optional. Loader and dependency updates are not required merely because newer compatible versions become available.

When the template itself needs a fix:

1. In IDEA, check out the relevant version branch, such as `26.2`.
2. Increase `mod_version` in `gradle.properties` when the published template version changes.
3. Make the fix and test Fabric and NeoForge.
4. Commit and push the version branch.
5. Create the next release tag, such as `mc26.2-1.0.1`, targeting `26.2`.

A fix that also applies to `dev` can be applied there separately. Avoid merging unrelated next-version development into an older Minecraft version branch.

## Policy

- `dev` contains the latest Minecraft development work.
- A branch such as `26.2` records the template released for that Minecraft version.
- Version branches have no continuous loader or dependency update obligation.
- Tags are created only after Fabric and NeoForge have both been tested.
- Existing release tags are immutable; publish a new patch version instead of moving a tag.
- This template does not use `alpha`, `beta`, or `rc` version suffixes.

The naming is inspired by Sodium, while the prerelease and long-term maintenance workflows are intentionally omitted for this template.

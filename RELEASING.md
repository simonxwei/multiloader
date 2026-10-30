# Release Guide

Template and derived-mod versions are independent.
Use [Development](DEVELOPMENT.md) for setup, checks, commits, and branch synchronization.

---

## Choose a Version and Target

Select a tested commit on the compatible Minecraft branch.

| Item             | Format / example                                                     |
|------------------|----------------------------------------------------------------------|
| Minecraft branch | `<minecraft_version>`                                                |
| Git tag          | `mc<minecraft_version>-<project_version>`; `mc26.3-0.1.0`            |
| Release title    | `<project_name> <project_version> for Minecraft <minecraft_version>` |
| Prerelease       | `-alpha.1`, `-beta.1`, or `-rc.1`; mark the Release **Pre-release**  |

| Project              | Version policy                                                                                            |
|----------------------|-----------------------------------------------------------------------------------------------------------|
| multiloader template | Starts at `0.1.0`, stays in `0.x.x`; Minor for meaningful capabilities/structure, Patch for smaller fixes |
| Derived mod          | Own version sequence in `mod_version`; the inherited `1.0.0` is only a starter value                      |

Follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html) with an optional prerelease suffix.
The Minecraft prefix records compatibility; changing Minecraft does not reset the project version sequence.
Equivalent feature baselines may share a project version across Minecraft branches, despite different implementations.
Older maintained series can receive later fixes: `mc26.2-0.1.4` may follow `mc26.3-0.2.0`.

## Verify the Release Candidate

1. Update version/metadata as applicable and commit.
2. From a clean checkout, perform the [build and runtime checks](DEVELOPMENT.md#build-and-test) for both loaders.
3. Regenerate affected data; test applicable persistence, networking, and migration behavior.
4. Inspect JAR contents, license, expanded metadata, and release notes. Record only checks actually performed.
5. If world data changes, include backup or migration instructions.

A successful build alone does not establish runtime compatibility.

## Check Generated Artifacts

Gradle uses `${mod_version}+mc${minecraft_version}` as the build version and `${mod_id}-${project.name}` as the archive name:

```text
<mod_id>-fabric-<mod_version>+mc<minecraft_version>.jar
<mod_id>-neoforge-<mod_version>+mc<minecraft_version>.jar
```

Put prerelease labels in `mod_version`, before `+mc...`.
Inspect actual files in each module's `build/libs/`; loaders may add classifiers.
Player downloads use distributable JARs. Source, Javadoc, and development JARs are separate artifacts.
GitHub's generated source archives contain source snapshots.
The template may release a source snapshot, and its tag version may differ from the example mod version inside demonstration JARs.

## Publish on GitHub

1. Push the verified commit to its Minecraft branch, following the [branch guide](DEVELOPMENT.md#synchronize-minecraft-branches).
2. Open **Releases → Draft a new release**; select a new tag and the verified target commit.
3. Add notes and, for a derived mod, the distributable Fabric and NeoForge JARs.
4. Recheck the target, tag, compatibility, prerelease flag, and every attachment; publish the draft.
5. Verify the published Release. Upload to distribution sites separately unless explicit automation handles them.

```text
<project_name> <project_version> for Minecraft <minecraft_version>

Changes: <key changes>
Verified: <checks actually performed>
Known limitations: <if any>
```

### Immutable Releases

[Immutable Releases](https://docs.github.com/en/code-security/concepts/supply-chain-security/immutable-releases) are optional; this template defaults to leaving them disabled.
If enabled, attach every artifact to the draft before publishing.
Published assets and the associated tag are locked; titles and notes remain editable.
Disabling the setting does not unlock existing immutable releases.
Their tag names cannot be reused after deletion, including after recreating a repository with the same name.

## Maintain Released Versions

- Keep published tags stable; release fixes with new versions and tags.
- Synchronize only compatible changes. Use reviewed merges or selected cherry-picks when branches diverge; respect PR requirements and avoid force-pushing published history.
- Backport selectively or adapt the implementation. Cherry-pick does not translate APIs.
- Test every Minecraft port independently and use matching project versions only for equivalent feature baselines.

Never commit secrets; revoke and rotate any that are exposed.

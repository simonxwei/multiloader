# Release Guide

The `multiloader` project and derived mods have independent version sequences.

For setup, architecture, conventions, and testing, see [Development](DEVELOPMENT.md).

---

## Versioning

Select a tested commit on the compatible Minecraft branch.

| Item             | Format / example                                                     |
|------------------|----------------------------------------------------------------------|
| Project name     | `rootProject.name`, not `mod_name`                                   |
| Minecraft branch | `<minecraft_version>`                                                |
| Git tag          | `mc<minecraft_version>-<project_version>`; e.g. `mc26.3-0.1.0`       |
| Release title    | `<project_name> <project_version> for Minecraft <minecraft_version>` |
| Prerelease       | `-alpha.1`, `-beta.1`, `-rc.1`; mark GitHub **Pre-release**          |

| Project       | Policy                                                                              |
|---------------|-------------------------------------------------------------------------------------|
| `multiloader` | Begins at `0.1.0`, stays in `0.x.x`; minor for significant changes, patch for fixes |
| Derived mod   | Own `mod_version`; inherited `1.0.0` is a starter value only                        |

Follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
Minecraft compatibility is encoded in the tag prefix, without resetting the project version.
Equivalent feature baselines may share versions across MC branches; supported older series may receive fixes.

## Release Checklist

- [ ] Commit version, metadata, and release notes.
- [ ] [Build and test](DEVELOPMENT.md#build-and-test) **both loaders**, including dedicated servers.
- [ ] Regenerate affected data; check persistence, networking, and migrations if applicable.
- [ ] Inspect JAR contents, license, expanded metadata, and distributable artifacts.
- [ ] Record checks performed, limitations, and backup/migration steps if needed.

A successful build does not replace runtime testing.

## Artifacts

Gradle version: `${mod_version}+mc${minecraft_version}`; archive base: `${mod_id}-${project.name}`.

```text
<mod_id>-fabric-<mod_version>+mc<minecraft_version>.jar
<mod_id>-neoforge-<mod_version>+mc<minecraft_version>.jar
```

Check actual files under each module's `build/libs/` for classifiers.
Prerelease suffixes belong in `mod_version`, before `+mc...`.
Distribute loader-ready JARs; development, source, and Javadoc artifacts are separate.
GitHub source archives are not mod binaries.

`multiloader` releases may be source-only and use tags independent of the example mod version.

## Version Maintenance

- Do not change released tags; issue fixes under new versions.
- Backport through reviewed merges or cherry-picks; test each Minecraft port and never force-push released history.
- Reuse project versions only for equivalent feature baselines; do not commit secrets.

See [Minecraft branch synchronization](DEVELOPMENT.md#synchronize-minecraft-branches).

## GitHub Releases

1. Push the verified commit to the target Minecraft branch.
2. Open **Releases → Draft a new release** and select a new tag at that commit.
3. Add notes and, for derived mods, Fabric and NeoForge JARs.
4. Verify tag, commit, compatibility, prerelease flag, and attachments; publish.
5. Publish to other distribution sites separately unless automated.

```text
<project_name> <project_version> for Minecraft <minecraft_version>

Changes: <key changes>
Verified: <checks performed>
Known limitations: <if any>
```

### Immutable Releases

[Immutable Releases](https://docs.github.com/en/code-security/concepts/supply-chain-security/immutable-releases) are optional and disabled by default.
When enabled, attach everything **before** publishing: assets and tags become locked, while titles and notes remain editable.
Disabling the setting does not unlock prior immutable releases or allow their deleted tag names to be reused.

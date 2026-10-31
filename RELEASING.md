# Release Guide

Template and derived mods have independent version sequences.
For development checks and branch operations, see [Development](DEVELOPMENT.md).

---

## Versioning

Select a tested commit on the compatible Minecraft branch.

| Item             | Format / example                                                     |
|------------------|----------------------------------------------------------------------|
| Minecraft branch | `<minecraft_version>`                                                |
| Git tag          | `mc<minecraft_version>-<project_version>`; e.g. `mc26.3-0.1.0`       |
| Release title    | `<project_name> <project_version> for Minecraft <minecraft_version>` |
| Prerelease       | `-alpha.1`, `-beta.1`, `-rc.1`; mark GitHub **Pre-release**          |

| Project     | Policy                                                                              |
|-------------|-------------------------------------------------------------------------------------|
| Template    | Begins at `0.1.0`, stays in `0.x.x`; minor for significant changes, patch for fixes |
| Derived mod | Uses its own `mod_version`; inherited `1.0.0` is only a starter value               |

Follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html) with optional prerelease suffixes.
Minecraft compatibility is recorded by the tag prefix, without resetting the project version.
Equivalent feature baselines may share versions across MC branches; older supported series may receive later fixes.

## Release Checklist

- [ ] Update and commit the version, metadata, and release notes.
- [ ] Run [build and runtime tests](DEVELOPMENT.md#build-and-test) on **both loaders**, including dedicated servers.
- [ ] Regenerate affected data; test applicable persistence, networking, and migrations.
- [ ] Inspect JAR contents, license, expanded metadata, and distributable artifacts.
- [ ] Document checks actually performed, known limitations, and world-backup/migration steps if needed.

Build success alone does not prove runtime compatibility.

## Artifacts

Gradle version: `${mod_version}+mc${minecraft_version}`; archive base name: `${mod_id}-${project.name}`.

```text
<mod_id>-fabric-<mod_version>+mc<minecraft_version>.jar
<mod_id>-neoforge-<mod_version>+mc<minecraft_version>.jar
```

Check actual files in each module's `build/libs/` for classifiers.
Put prerelease labels in `mod_version`, before `+mc...`. Distribute loader-ready JARs; source, Javadoc, and development JARs are separate.
GitHub-generated source archives are snapshots, not mod binaries.

The template may publish source-only releases whose tags differ from the demonstration JAR version.

## Version Maintenance

- Keep published tags stable; issue fixes with new versions and tags.
- Backport compatible changes through reviewed merges or selective cherry-picks; never force-push released history.
- Adapt APIs and test each Minecraft port independently. Reuse project versions only for equivalent feature baselines.
- Never commit secrets; revoke and rotate exposed credentials.

See [Minecraft branch synchronization](DEVELOPMENT.md#synchronize-minecraft-branches).

## GitHub Releases

1. Push the verified commit to the correct Minecraft branch.
2. Open **Releases → Draft a new release**; set a new tag and the verified commit.
3. Add notes and, for a derived mod, the Fabric and NeoForge distributable JARs.
4. Check tag, commit, compatibility, prerelease flag, and attachments; publish and verify.
5. Publish to mod distribution sites separately unless explicitly automated.

```text
<project_name> <project_version> for Minecraft <minecraft_version>

Changes: <key changes>
Verified: <checks performed>
Known limitations: <if any>
```

### Immutable Releases

[Immutable Releases](https://docs.github.com/en/code-security/concepts/supply-chain-security/immutable-releases) are optional and disabled by default in this template.
If enabled, attach everything **before** publishing: assets and tags become locked, while titles and notes remain editable.
Disabling the feature does not unlock existing immutable releases; their deleted tag names cannot be reused, even in a recreated repository of the same name.

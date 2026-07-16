# Releasing

**This repository uses a simplified Sodium-style branch and tag layout.** It keeps only stable releases because the template can be changed and tested directly on the development branch.

---

## Names

| Item | Format | Example |
| --- | --- | --- |
| Latest development branch | `dev` | `dev` |
| Stable maintenance branch | `<minecraft_version>/stable` | `26.2/stable` |
| Stable annotated tag | `mc<minecraft_version>-<mod_version>` | `mc26.2-1.0.0` |
| GitHub release title | `multiloader <mod_version> for Minecraft <minecraft_version>` | `multiloader 1.0.0 for Minecraft 26.2` |

`minecraft_version` and `mod_version` remain separate properties in `gradle.properties`. A Minecraft port does not automatically require a new mod version, while a template change can increase `mod_version` without changing Minecraft compatibility.

One Git tag represents the common, Fabric, and NeoForge outputs together. If an external publishing platform requires loader-specific version identifiers, append the loader only there:

```text
mc26.2-1.0.0-fabric
mc26.2-1.0.0-neoforge
```

These are platform version identifiers, not separate Git tags.

## One-time default branch setup

Rename `master` to `dev` locally and publish it:

```shell
git switch master
git pull --ff-only
git branch -m dev
git push -u origin dev
```

In GitHub, open **Settings → Branches**, change the default branch from `master` to `dev`, and then remove the old remote branch:

```shell
git push origin --delete master
```

Do not delete `master` before GitHub uses `dev` as the default branch.

## Freeze the current Minecraft version

After the current `dev` commit has been tested on both loaders, create its stable maintenance branch and first tag. For the current template:

```shell
git switch dev
git pull --ff-only

git switch -c 26.2/stable
git push -u origin 26.2/stable

git tag -a mc26.2-1.0.0 -m "multiloader 1.0.0 for Minecraft 26.2"
git push origin mc26.2-1.0.0

git switch dev
```

The branch and tag initially point to the same commit. The branch may later receive compatible fixes; the tag must never move. `dev` can immediately begin the next Minecraft port.

## Release a stable fix

Make fixes for an older Minecraft version on its stable branch. Increase `mod_version` when the template itself changes, test both loaders, and create a new tag:

```shell
git switch 26.2/stable
git pull --ff-only

# Edit mod_version in gradle.properties, then make and test the fix.
git add .
git commit -m "Fix 26.2 template issue"
git push

git tag -a mc26.2-1.0.1 -m "multiloader 1.0.1 for Minecraft 26.2"
git push origin mc26.2-1.0.1
```

Create a GitHub Release from that tag when a visible stable download page is useful.

## Move fixes between branches

A fix that also applies to another maintained line should be copied with `cherry-pick` rather than merging the entire development branch into an older stable branch:

```shell
git switch 26.2/stable
git cherry-pick <commit>
```

Resolve and test the result on that Minecraft version before pushing. Never rewrite or retarget an already published stable tag; create the next patch version instead.

## Development policy

- Untagged commits on `dev` may be experimental or target snapshots.
- Stable branches accept only changes compatible with their named Minecraft version.
- Tags are created only after Fabric and NeoForge have both been tested.
- This template does not use `alpha`, `beta`, or `rc` version suffixes.

The branch and tag naming is inspired by [Sodium](https://github.com/CaffeineMC/sodium), with the prerelease workflow intentionally omitted for this template.

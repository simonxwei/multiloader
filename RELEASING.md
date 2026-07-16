# Releasing

**This repository uses `dev` for the latest Minecraft development line.**
Each released Minecraft version has its own branch.
Only final template releases are published.

---

## Naming

| Item                      | Format                                                        |
|---------------------------|---------------------------------------------------------------|
| Latest development branch | `dev`                                                         |
| Minecraft version branch  | `<minecraft_version>`                                         |
| Release tag               | `mc<minecraft_version>-<mod_version>`                         |
| GitHub release title      | `multiloader <mod_version> for Minecraft <minecraft_version>` |

Use the current `minecraft_version` and `mod_version` values from `gradle.properties`.
Keep them separate.
One Git tag represents the common, Fabric, and NeoForge outputs together.

## Verify

Run from the repository root:

```shell
./gradlew clean build --warning-mode all
./gradlew :fabric:validateAccessWidener --warning-mode all
./gradlew publishToMavenLocal
```

The build also validates NeoForge Access Transformer targets.
`validateAccessTransformers` is enabled in both `common` and `neoforge`.
`publishToMavenLocal` is only a publication smoke test.
It writes to the current user's Maven local repository.

## Publish a Minecraft Version

1. Push the tested commit to `dev`.
2. Create and check out a branch from `dev` named with the current `minecraft_version`.
3. Push the new branch, then switch back to `dev` for future development.
4. On GitHub, draft a release targeting that version branch.
   Create `mc<minecraft_version>-<mod_version>` as the release tag.
5. Use `multiloader <mod_version> for Minecraft <minecraft_version>` as the title.
   Attach any desired build outputs and publish without marking the release as a prerelease.

A concise release description is sufficient:

```markdown
Minecraft <minecraft_version> multiloader template for Fabric and NeoForge.

Tested on both loaders.
```

## Update a Released Version

Updating an older version branch is optional.
When the template itself needs a fix:

1. Check out the corresponding Minecraft version branch.
2. Increase `mod_version` when publishing a new template version.
3. Apply the fix, verify both loaders, and push the branch.
4. Create a new tag with the updated `mod_version`.

Never move or reuse an existing release tag.
Apply fixes to `dev` separately when they are still relevant there.
Do not merge unrelated next-version work into an older branch.

## Policy

- Version branches record released templates; they do not require continuous loader or dependency updates.
- Tags are created only after Fabric and NeoForge have both been tested.
- One tag covers all three project outputs.
- Existing tags are immutable.
- Alpha, beta, and release-candidate suffixes are not used.

# Releasing

This file documents the release policy used by the **multiloader template repository itself**.

It is an example workflow, not a requirement for mods created from the template.
Adopters should replace it when their own versioning or maintenance policy differs.

The instructions intentionally avoid binding `dev` to a specific Minecraft version.
The active target and toolchain are always the values currently stored in `gradle.properties`.

---

## Repository Convention

| Item | Template repository convention |
|---|---|
| Development branch | `dev` |
| Released Minecraft branch | `<minecraft_version>` |
| Release tag | `mc<minecraft_version>-<mod_version>` |
| Release title | `multiloader <mod_version> for Minecraft <minecraft_version>` |

One tag represents the common, Fabric, and NeoForge outputs from the same tested commit.

## Verify

Run from the repository root:

```shell
./gradlew clean build --warning-mode all
./gradlew :fabric:validateAccessWidener --warning-mode all
./gradlew publishToMavenLocal
```

Then verify all four physical environments:

```shell
./gradlew :fabric:runClient
./gradlew :neoforge:runClient
./gradlew :fabric:runServer
./gradlew :neoforge:runServer
```

Dedicated servers are console-only.
Accept the EULA in each run directory when required, wait for the `Done` message, and enter `stop` for a clean shutdown.
`publishToMavenLocal` is only a publication smoke test.

## Create a Template Release

1. Verify the intended commit on `dev`.
2. Create a branch named with the current `minecraft_version` from that commit.
3. Push the version branch and continue later development on `dev`.
4. Draft a GitHub release targeting the version branch.
5. Create the tag `mc<minecraft_version>-<mod_version>` and use the corresponding release title.
6. Attach desired artifacts and publish the release.

A concise description is sufficient:

```markdown
Minecraft <minecraft_version> multiloader template for Fabric and NeoForge.

Tested on both loaders and on dedicated servers.
```

## Maintain a Released Branch

Maintenance of an older version branch is optional.
When the template itself needs a fix:

1. Apply the fix to the affected version branch.
2. Increase `mod_version` for a new published template build.
3. Repeat the verification matrix.
4. Create a new tag; never move or reuse an existing tag.
5. Apply the same fix to `dev` separately when it remains relevant there.

Version branches record released template states and do not require continuous loader or dependency updates.
Pre-release naming, long-term support, and the number of maintained branches are repository choices rather than constraints imposed on generated mods.

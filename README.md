# Void One Block

**A minimalist, mod-compatible one-block-void world type for Forge 1.16.5.**

Start with nothing but a single block beneath your feet — and everything else is up to you.

***

## What is this?

Void One Block adds a brand-new **world type** to Minecraft. Instead of generating normal terrain, it strips everything away and leaves you standing on exactly one block, floating in the void. From there, it's your job to survive, expand, and explore — while structures, biomes, and mod content still work exactly as they should.

No mountains. No oceans. No forests. Just you, one block, and the rest of the world waiting to be found through exploration, structures, and progression.

***

## Key Features

### 🧱 True One-Block Start

The Overworld spawns you on a single platform block above an endless void. The block regenerates itself if it's ever removed by outside means (but never overwrites anything you've placed — it only fills in plain air).

### 🌌 Void Nether & Void End

Take the challenge further — the Nether and the End can be voided too:

*   **Nether:** stripped of terrain, but fortresses, bastions, and modded Nether structures still generate normally.
*   **The End:** islands are gone, but the exit fountain, obsidian pillars, and End Cities still spawn correctly — including a fully working dragon fight and podium sequence.

Both are optional and configurable independently.

### 🧩 Full Mod & Biome Compatibility

This isn't a "delete everything" void generator. Original biome sources are preserved, meaning:

*   Modded biomes (BYG, BOP, and similar) still assign their correct biome IDs to your world.
*   Modded structures still use their normal generation pipeline.
*   Only ordinary terrain and decoration (trees, ores, terrain blobs, islands) are removed — structures and biome identity stay intact.

### ⚙️ Configurable Spawn Block

Choose exactly what you start on. By default, Void One Block pairs with **Random Item Generator Resurrected**, spawning you on its mystery item block for an extra layer of unpredictability. Don't have that mod installed? No problem — it automatically falls back to plain `minecraft:bedrock` so the world always generates safely.

```
spawnBlock = "random_item_generator_resurrected:item_block"
```

### 🗝️ Structures, Preserved

`generateStructures` is on by default, so strongholds, End Cities, fortresses, bastions, villages, and modded structures all remain part of the experience — you just won't find them buried in useless terrain.

### 🎁 Bonus Chest Support

Enabled the bonus chest at world creation? It'll spawn right beside your starting block, using the standard vanilla loot table — one time only, and it won't reappear once it's been placed.

### 🌀 Safe End Gateways

End gateways get a small safety platform automatically, so stepping through one in a void End won't strand you in empty space.

### 🧭 No Experimental Warnings

World generation settings are marked stable, so you won't get the "This world uses experimental settings" pop-up when creating a Void One Block world.

***

## Configuration Options

| Option             |Default                                      |Description                                                                                                                        |
| ------------------ |-------------------------------------------- |---------------------------------------------------------------------------------------------------------------------------------- |
| <code>generateStructures</code> |<code>true</code>                            |Enables/disables structure generation across all void dimensions.                                                                  |
| <code>spawnBlock</code> |<code>random_item_generator_resurrected:item_block</code> |The block placed beneath the player at Overworld spawn. Falls back to <code>minecraft:bedrock</code> if the configured block/mod isn't present. |
| <code>voidNether</code> |<code>true</code>                            |Voids Nether terrain while preserving structures and biomes.                                                                       |
| <code>voidEnd</code> |<code>true</code>                            |Voids End terrain while preserving the fountain, pillars, End Cities, and dragon fight.                                            |

***

## Recommended Pairing

Void One Block is designed to work great on its own, but it shines alongside **Random Item Generator Resurrected** — your starting block becomes a mystery item source, turning the classic "one block" survival challenge into a proper progression loop.

_(Random Item Generator Resurrected is an optional dependency — Void One Block works fine without it.)_

***

## Getting Started

1.  Install the mod (and optionally Random Item Generator Resurrected).
2.  Create a new world.
3.  Select the **Void One Block** world type.
4.  Fall into the void, land on your block, and start surviving.

***

_Built for Forge 1.16.5._

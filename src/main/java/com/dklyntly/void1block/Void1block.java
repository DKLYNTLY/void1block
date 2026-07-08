package com.dklyntly.void1block;

import com.dklyntly.void1block.config.Void1BlockConfig;
import com.dklyntly.void1block.world.DragonFogHandler;
import com.dklyntly.void1block.world.EndGatewayHandler;
import com.dklyntly.void1block.world.EndPodiumHandler;
import com.dklyntly.void1block.world.OneBlockChunkGenerator;
import com.dklyntly.void1block.world.OneBlockSpawnHandler;
import com.dklyntly.void1block.world.VoidDimensionChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Ported from the 1.16.5 version.
 *
 * BIG STRUCTURAL CHANGE: there is no more ForgeWorldType / custom world-type
 * factory in 1.20.1 Forge - Mojang removed the old "pick a world type"
 * system entirely. Custom world presets are now defined as data, not code:
 *
 *   data/void1block/worldgen/dimension/one_block_overworld.json  (references OneBlockChunkGenerator.CODEC)
 *   data/void1block/worldgen/dimension/one_block_nether.json     (references VoidDimensionChunkGenerator.CODEC)
 *   data/void1block/worldgen/dimension/one_block_end.json        (references VoidDimensionChunkGenerator.CODEC, end_specials: true)
 *   data/void1block/worldgen/world_preset/void_one_block.json    (bundles the three dimensions above)
 *   data/minecraft/tags/worldgen/world_preset/normal.json        (adds our preset to the visible list)
 *   assets/void1block/lang/en_us.json                            (display name for the preset)
 *
 * All Java has to do now is register the two ChunkGenerator codecs so the
 * dimension JSONs above can reference them by id, register the config, and
 * keep the runtime spawn/podium/gateway/fog handlers.
 *
 * IMPORTANT LIMITATION: because the world preset's dimensions are now fixed
 * datapack JSON rather than something built in Java at world-creation time,
 * the old runtime voidNether()/voidEnd() config toggles can no longer swap
 * which generator a preset uses - that's baked into the JSON at build time.
 * The "Void One Block" preset below always uses the void generator for the
 * Nether and End. If you want a toggle again, the clean way is to ship two
 * presets (e.g. void_one_block and void_one_block_normal_nether) and let the
 * player pick the right one from the Create World screen.
 */
@Mod(Void1block.MOD_ID)
public class Void1block {

    public static final String MOD_ID = "void1block";

    public Void1block() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(Void1block::registerChunkGenerators);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                Void1BlockConfig.SPEC
        );

        MinecraftForge.EVENT_BUS.register(new OneBlockSpawnHandler());
        MinecraftForge.EVENT_BUS.register(new EndPodiumHandler());
        MinecraftForge.EVENT_BUS.register(new EndGatewayHandler());
        MinecraftForge.EVENT_BUS.register(new DragonFogHandler());
    }

    private static void registerChunkGenerators(final RegisterEvent event) {
        event.register(Registries.CHUNK_GENERATOR, helper -> {
            helper.register(new ResourceLocation(MOD_ID, "one_block"), OneBlockChunkGenerator.CODEC);
            helper.register(new ResourceLocation(MOD_ID, "void_dimension"), VoidDimensionChunkGenerator.CODEC);
        });
    }
}

package com.dklyntly.void1block;

import com.dklyntly.void1block.config.Void1BlockConfig;
import com.dklyntly.void1block.world.DragonFogHandler;
import com.dklyntly.void1block.world.EndGatewayHandler;
import com.dklyntly.void1block.world.EndPodiumHandler;
import com.dklyntly.void1block.world.OneBlockChunkGenerator;
import com.dklyntly.void1block.world.OneBlockSpawnHandler;
import com.dklyntly.void1block.world.VoidDimensionChunkGenerator;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Void1block.MOD_ID)
public class Void1block {

    public static final String MOD_ID = "void1block";

    private static final DeferredRegister<ForgeWorldType> WORLD_TYPES =
            DeferredRegister.create(ForgeRegistries.WORLD_TYPES, MOD_ID);

    public static final RegistryObject<ForgeWorldType> VOID_ONE_BLOCK =
            WORLD_TYPES.register(
                    "void_one_block",
                    () -> new ForgeWorldType(new OneBlockWorldTypeFactory())
            );

    public Void1block() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        WORLD_TYPES.register(modBus);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                Void1BlockConfig.SPEC
        );

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new OneBlockSpawnHandler());
        MinecraftForge.EVENT_BUS.register(new EndPodiumHandler());
        MinecraftForge.EVENT_BUS.register(new EndGatewayHandler());
        MinecraftForge.EVENT_BUS.register(new DragonFogHandler());
    }

    private static class OneBlockWorldTypeFactory
            implements ForgeWorldType.IChunkGeneratorFactory {

        @Override
        public ChunkGenerator createChunkGenerator(
                Registry<Biome> biomeRegistry,
                Registry<DimensionSettings> dimensionSettingsRegistry,
                long seed,
                String generatorSettings
        ) {
            return new OneBlockChunkGenerator(
                    DimensionGeneratorSettings
                            .makeDefaultOverworld(
                                    biomeRegistry,
                                    dimensionSettingsRegistry,
                                    seed
                            )
                            .getBiomeSource()
            );
        }

        @Override
        public DimensionGeneratorSettings createSettings(
                DynamicRegistries dynamicRegistries,
                long seed,
                boolean ignoredGenerateStructures,
                boolean bonusChest,
                String generatorSettings
        ) {
            Registry<Biome> biomeRegistry =
                    dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);

            Registry<DimensionType> dimensionTypeRegistry =
                    dynamicRegistries.registryOrThrow(
                            Registry.DIMENSION_TYPE_REGISTRY
                    );

            Registry<DimensionSettings> dimensionSettingsRegistry =
                    dynamicRegistries.registryOrThrow(
                            Registry.NOISE_GENERATOR_SETTINGS_REGISTRY
                    );

            SimpleRegistry<Dimension> defaultDimensions =
                    DimensionType.defaultDimensions(
                            dimensionTypeRegistry,
                            biomeRegistry,
                            dimensionSettingsRegistry,
                            seed
                    );

            SimpleRegistry<Dimension> dimensions =
                    DimensionGeneratorSettings.withOverworld(
                            dimensionTypeRegistry,
                            defaultDimensions,
                            new OneBlockChunkGenerator(
                                    DimensionGeneratorSettings
                                            .makeDefaultOverworld(
                                                    biomeRegistry,
                                                    dimensionSettingsRegistry,
                                                    seed
                                            )
                                            .getBiomeSource()
                            )
                    );

            return new DimensionGeneratorSettings(
                    seed,
                    false,
                    bonusChest,
                    withOptionalVoidDimensions(
                            dimensions,
                            dimensionTypeRegistry
                    )
            );
        }

        private SimpleRegistry<Dimension> withOptionalVoidDimensions(
                SimpleRegistry<Dimension> source,
                Registry<DimensionType> dimensionTypeRegistry
        ) {
            SimpleRegistry<Dimension> dimensions = new SimpleRegistry<>(
                    Registry.LEVEL_STEM_REGISTRY,
                    Lifecycle.stable()
            );

            for (java.util.Map.Entry<RegistryKey<Dimension>, Dimension> entry
                    : source.entrySet()) {

                RegistryKey<Dimension> key = entry.getKey();
                Dimension dimension = entry.getValue();

                if (key == Dimension.NETHER && Void1BlockConfig.voidNether()) {
                    dimensions.register(
                            key,
                            new Dimension(
                                    () -> dimensionTypeRegistry.getOrThrow(
                                            DimensionType.NETHER_LOCATION
                                    ),
                                    new VoidDimensionChunkGenerator(
                                            dimension.generator().getBiomeSource()
                                    )
                            ),
                            Lifecycle.stable()
                    );
                } else if (key == Dimension.END && Void1BlockConfig.voidEnd()) {
                    dimensions.register(
                            key,
                            new Dimension(
                                    () -> dimensionTypeRegistry.getOrThrow(
                                            DimensionType.END_LOCATION
                                    ),
                                    new VoidDimensionChunkGenerator(
                                            dimension.generator().getBiomeSource(),
                                            true
                                    )
                            ),
                            Lifecycle.stable()
                    );
                } else {
                    dimensions.register(key, dimension, Lifecycle.stable());
                }
            }

            return dimensions;
        }
    }

    @Mod.EventBusSubscriber(
            modid = MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerChunkGenerators(
                final RegistryEvent.Register<ForgeWorldType> event
        ) {
            Registry.register(
                    Registry.CHUNK_GENERATOR,
                    new ResourceLocation(MOD_ID, "one_block"),
                    OneBlockChunkGenerator.CODEC
            );

            Registry.register(
                    Registry.CHUNK_GENERATOR,
                    new ResourceLocation(MOD_ID, "void_dimension"),
                    VoidDimensionChunkGenerator.CODEC
            );
        }
    }
}

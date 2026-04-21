package com.relictales.registry.processors;

import com.mojang.serialization.MapCodec;
import com.relictales.RelicTales;
import com.relictales.content.structure.JungleTempleProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, RelicTales.MOD_ID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<JungleTempleProcessor>> JUNGLE_TEMPLE_PROCESSOR =
            PROCESSORS.register("jungle_temple_processor", () -> () -> JungleTempleProcessor.CODEC);

    public static void init() {
    }
}

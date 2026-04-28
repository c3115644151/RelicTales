package com.relictales.content.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.relictales.RelicTales;
import com.relictales.registry.blocks.RelicBlocks;
import com.relictales.registry.processors.ModStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class EndCityProcessor extends StructureProcessor {

    public static final MapCodec<EndCityProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("replacement_chance").forGetter(p -> p.replacementChance)
            ).apply(instance, EndCityProcessor::new)
    );

    private final float replacementChance;

    public EndCityProcessor(float replacementChance) {
        this.replacementChance = replacementChance;
    }

    @Override
    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo originalBlockInfo,
            StructureTemplate.StructureBlockInfo currentBlockInfo,
            StructurePlaceSettings settings
    ) {
        if (currentBlockInfo.state().is(Blocks.PURPUR_BLOCK)) {
            RandomSource random = settings.getRandom(currentBlockInfo.pos());
            if (random.nextFloat() < replacementChance) {
                return new StructureTemplate.StructureBlockInfo(
                        currentBlockInfo.pos(),
                        RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get().defaultBlockState(),
                        currentBlockInfo.nbt()
                );
            }
        }
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.END_CITY_PROCESSOR.get();
    }
}

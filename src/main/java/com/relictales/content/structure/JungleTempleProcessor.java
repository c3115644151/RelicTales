package com.relictales.content.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.relictales.registry.blocks.RelicBlocks;
import com.relictales.registry.processors.ModStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * 丛林神殿结构处理器：将部分苔石砖替换为可疑苔石砖。
 * 通过 {@code data/minecraft/worldgen/processor_list/jungle_temple.json} 应用。
 */
@SuppressWarnings("deprecation")
public class JungleTempleProcessor extends StructureProcessor {

    public static final MapCodec<JungleTempleProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new JungleTempleProcessor())
    );

    // 替换概率：20% 的苔石砖变为可疑版本
    private static final float REPLACEMENT_CHANCE = 0.20f;

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
        if (currentBlockInfo.state().is(Blocks.MOSSY_STONE_BRICKS)) {
            RandomSource random = settings.getRandom(currentBlockInfo.pos());
            if (random.nextFloat() < REPLACEMENT_CHANCE) {
                return new StructureTemplate.StructureBlockInfo(
                        currentBlockInfo.pos(),
                        RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get().defaultBlockState(),
                        currentBlockInfo.nbt()
                );
            }
        }
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.JUNGLE_TEMPLE_PROCESSOR.get();
    }
}

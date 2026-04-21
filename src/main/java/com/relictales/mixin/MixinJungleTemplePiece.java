package com.relictales.mixin;

import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.JungleTemplePiece;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(JungleTemplePiece.class)
public abstract class MixinJungleTemplePiece {

    @Inject(method = "postProcess", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void relictales$replaceMossyBricks(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator chunkGenerator,
            RandomSource random,
            BoundingBox boundingBox,
            ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        // At TAIL, all blocks have been placed. Scan and replace MOSSY_STONE_BRICKS.
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int minX = Math.min(boundingBox.minX(), referencePos.getX() - 11);
        int maxX = Math.max(boundingBox.maxX(), referencePos.getX() + 11);
        int minY = Math.max(boundingBox.minY(), referencePos.getY() - 10);
        int maxY = Math.min(boundingBox.maxY(), referencePos.getY() + 10);
        int minZ = Math.min(boundingBox.minZ(), referencePos.getZ() - 14);
        int maxZ = Math.max(boundingBox.maxZ(), referencePos.getZ() + 14);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutablePos.set(x, y, z);
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.is(Blocks.MOSSY_STONE_BRICKS)) {
                        level.setBlock(mutablePos,
                            RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}
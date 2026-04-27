package com.relictales.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = StructurePiece.class, remap = false)
public interface StructurePieceInvoker {

    @Invoker(value = "getWorldPos", remap = false)
    BlockPos.MutableBlockPos invokeGetWorldPos(int x, int y, int z);

    @Invoker(value = "placeBlock", remap = false)
    void invokePlaceBlock(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB);
}

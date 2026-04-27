package com.relictales.mixin;

import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces$RoomCrossing", remap = false)
public interface RoomCrossingAccessor {

    @Accessor("type")
    int relictales$getType();
}

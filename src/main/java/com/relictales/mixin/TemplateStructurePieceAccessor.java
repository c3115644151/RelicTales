package com.relictales.mixin;

import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TemplateStructurePiece.class, remap = false)
public interface TemplateStructurePieceAccessor {

    @Accessor(value = "placeSettings", remap = false)
    StructurePlaceSettings relictales$getPlaceSettings();
}

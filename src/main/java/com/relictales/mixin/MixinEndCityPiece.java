package com.relictales.mixin;

import com.relictales.RelicTales;
import com.relictales.content.structure.EndCityProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Injects per-room-type StructureProcessor into EndCityPiece via constructor TAIL.
 *
 * EndCityPiece extends TemplateStructurePiece, which stores the private
 * "placeSettings" field. We use TemplateStructurePieceAccessor to reach it.
 *
 * The constructor receives the raw template name ("base_floor", "ship", etc.)
 * as its second parameter. We read it, look up the room-specific probability,
 * and add an EndCityProcessor to the piece's placeSettings.
 */
@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.EndCityPieces$EndCityPiece", remap = false)
public abstract class MixinEndCityPiece {

    /**
     * Per-room-type replacement probabilities.
     *
     * Templates not listed (tower_base, tower_floor, tower_piece, etc.)
     * get 0% — they contain purpur pillars or other structural blocks
     * that should not be replaced.
     */
    private static final Map<String, Float> TEMPLATE_CHANCES = new HashMap<>(Map.ofEntries(
            Map.entry("base_floor",           0.05f),
            Map.entry("second_floor_1",        0.04f),
            Map.entry("second_floor_2",        0.04f),
            Map.entry("third_floor_1",         0.03f),
            Map.entry("third_floor_2",         0.10f),
            Map.entry("fat_tower_base",        0.05f),
            Map.entry("fat_tower_middle",      0.04f),
            Map.entry("fat_tower_top",         0.12f),
            Map.entry("tower_top",             0.06f),
            Map.entry("ship",                  0.08f),
            Map.entry("bridge_end",            0.01f),
            Map.entry("bridge_gentle_stairs",  0.02f),
            Map.entry("bridge_steep_stairs",   0.02f),
            Map.entry("bridge_start",          0.02f),
            Map.entry("base_roof",             0.02f),
            Map.entry("second_roof",           0.02f),
            Map.entry("third_roof",            0.02f)
    ));

    /**
     * Constructor injection — runtime piece creation during worldgen.
     *
     * EndCityPiece(StructureTemplateManager, String templateName,
     *              BlockPos, Rotation, boolean overwrite)
     *
     * After the constructor finishes, placeSettings is initialized.
     * We check templateName and add a per-room processor.
     */
    @Inject(method = "<init>(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Ljava/lang/String;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Z)V",
            at = @At("RETURN"), remap = false)
    private void relictales$addPerRoomProcessor(
            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager manager,
            String name,
            net.minecraft.core.BlockPos pos,
            net.minecraft.world.level.block.Rotation rotation,
            boolean overwrite,
            CallbackInfo ci) {
        float chance = TEMPLATE_CHANCES.getOrDefault(name, 0.0f);
        if (chance > 0) {
            TemplateStructurePieceAccessor accessor =
                    (TemplateStructurePieceAccessor) (Object) this;
            var settings = accessor.relictales$getPlaceSettings();
            settings.addProcessor(new EndCityProcessor(chance));
            RelicTales.LOGGER.info("[EC Mixin] Added EndCityProcessor(chance={}) to '{}'", chance, name);
        } else {
            RelicTales.LOGGER.info("[EC Mixin] Skipped '{}' (chance={})", name, chance);
        }
    }
}

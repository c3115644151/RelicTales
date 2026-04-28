package com.relictales.mixin;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles End City suspicious block injection via postProcess TAIL scanning.
 *
 * EndCityPiece extends TemplateStructurePiece, which uses StructureTemplate-based
 * block placement. The StructureProcessor approach (adding a processor to
 * placeSettings) does NOT work in NeoForge 26.1 — the processor's processBlock
 * is never called during template.placeInWorld(). Instead, we scan the
 * bounding box after the template is placed and replace purpur blocks
 * deterministically.
 *
 * We target TemplateStructurePiece (the parent class) because Mixin cannot
 * resolve the inherited postProcess method when targeting the inner class
 * EndCityPieces$EndCityPiece directly.
 */
@Mixin(value = TemplateStructurePiece.class, remap = false)
public abstract class MixinEndCityProcessor {

    private static final Logger LOG = LoggerFactory.getLogger("relictales");
    private static final ResourceKey<LootTable> END_CITY_LOOT =
            ResourceKey.create(Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_purpur_block"));

    /**
     * Per-room-type replacement probabilities.
     *
     * Templates not listed (tower_base, tower_floor, tower_piece, bridge_piece)
     * get 0% — they contain purpur pillars, spiral stairs, or bridge spans
     * whose structural blocks should not be replaced.
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
            Map.entry("bridge_end",            0.20f),
            Map.entry("bridge_gentle_stairs",  0.02f),
            Map.entry("bridge_steep_stairs",   0.02f),
            Map.entry("base_roof",             0.02f),
            Map.entry("second_roof",           0.02f),
            Map.entry("third_roof",            0.02f)
    ));

    /**
     * Deterministic random check for block replacement.
     * Uses world seed + position to create a reproducible random decision.
     */
    private static boolean shouldReplace(WorldGenLevel level, BlockPos pos, float chance) {
        return switch ((int) (chance * 100)) {
            case 0 -> false;
            case 20 -> { // bridge_end: 20%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x3333; // 20% = ~13107/65536
            }
            case 12 -> { // fat_tower_top: 12%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x1EB8; // 12% = ~7860/65536
            }
            case 10 -> { // third_floor_2: 10%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x199A; // 10% = ~6554/65536
            }
            case 8 -> { // ship: 8%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x147B; // 8% = ~5243/65536
            }
            case 6 -> { // tower_top: 6%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x0F5C; // 6% = ~3932/65536
            }
            case 5 -> { // base_floor, fat_tower_base: 5%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x0CCC; // 5% = ~3277/65536
            }
            case 4 -> { // second_floor_1/2, fat_tower_middle: 4%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x0A3D; // 4% = ~2621/65536
            }
            case 3 -> { // third_floor_1: 3%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x07AE; // 3% = ~1966/65536
            }
            case 2 -> { // roof/stairs: 2%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x051E; // 2% = ~1311/65536
            }
            case 1 -> { // bridge_end: 1%
                long seed = level.getSeed() ^ (long) pos.getX() * 198491317L ^ (long) pos.getY() * 6542987L ^ (long) pos.getZ() * 357239L;
                yield (seed & 0xFFFF) < 0x028F; // 1% = ~655/65536
            }
            default -> { // other (unused) — use standard random
                yield level.getRandom().nextFloat() < chance;
            }
        };
    }

    /**
     * Only replace blocks visible from player-accessible positions.
     * Walking surfaces (air above) and wall interiors (air on horizontal face)
     * are included. Platform bottoms (only air below), pillar cores, and
     * exterior-only faces are excluded.
     */
    private static boolean isInteriorSurface(WorldGenLevel level, BlockPos pos) {
        // Walking surface: player stands on it or it's a visible step
        if (level.getBlockState(pos.above()).isAir()) return true;
        // Wall surface: visible from inside the room
        if (level.getBlockState(pos.north()).isAir()) return true;
        if (level.getBlockState(pos.south()).isAir()) return true;
        if (level.getBlockState(pos.west()).isAir()) return true;
        if (level.getBlockState(pos.east()).isAir()) return true;
        // Platform bottom, roof exterior, pillar core: skip
        return false;
    }

    /**
     * Force-replace the one purpur block directly behind the Elytra ItemFrame.
     *
     * We find the Elytra marker via filterBlocks, reading metadata from the
     * template's block entity NBT directly — same approach as
     * TemplateStructurePiece.postProcess itself (which calls
     * info.nbt().getString("metadata") for handleDataMarker).
     *
     * The ItemFrame faces `rotation * SOUTH`, so the block behind the frame
     * (the one it's mounted on) is at
     * `markerPos.relative((rotation * SOUTH).getOpposite())`.
     *
     * This mirrors the Nether Fortress lava well pattern: local coordinate
     * check → 100% bypass of probability + visibility filters for exactly one
     * block.
     */
    private static void handleElytraBlock(List<BlockPos> toReplace, WorldGenLevel level, TemplateStructurePiece self) {
        StructureTemplate template = getTemplateField(self);
        StructurePlaceSettings settings = getPlaceSettings(self);
        BlockPos templatePos = getTemplatePosition(self);
        if (template == null || settings == null || templatePos == null) {
            LOG.warn("[EC Elytra] Reflection failed: template={}, settings={}, templatePos={}",
                    template, settings, templatePos);
            return;
        }

        var markers = template.filterBlocks(templatePos, settings, Blocks.STRUCTURE_BLOCK);
        LOG.info("[EC Elytra] Found {} structure_block markers in 'ship' template", markers.size());

        for (var marker : markers) {
            BlockPos worldPos = marker.pos();
            var nbt = marker.nbt();
            String metadata = (nbt != null) ? nbt.getString("metadata").orElse("") : "NO_NBT";
            LOG.info("[EC Elytra] Marker at worldPos={}, metadata='{}'", worldPos, metadata);

            if (nbt != null && "Elytra".equals(metadata)) {
                Direction frameFacing = settings.getRotation().rotate(Direction.SOUTH);
                BlockPos behind = worldPos.relative(frameFacing.getOpposite());
                BlockState behindState = level.getBlockState(behind);
                LOG.info("[EC Elytra] Elytra marker at {}; frameFacing={}, behind={}, behindState={}",
                        worldPos, frameFacing, behind, behindState.getBlock());

                if (behindState.is(Blocks.PURPUR_BLOCK) && !toReplace.contains(behind)) {
                    toReplace.add(behind);
                    LOG.info("[EC Elytra] Added {} to replacement list", behind);
                } else {
                    LOG.warn("[EC Elytra] Block behind elytra is {}, not PURPUR_BLOCK — skipping",
                            behindState.getBlock());
                }
                break;
            }
        }

        if (markers.isEmpty() && toReplace.isEmpty()) {
            LOG.warn("[EC Elytra] No markers found AND no blocks to replace — ship handler did nothing!");
        }
    }

    @Inject(method = "postProcess", at = @At("TAIL"), remap = false)
    private void relictales$onEndCityPostProcess(
            WorldGenLevel level,
            net.minecraft.world.level.StructureManager structureManager,
            net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator,
            RandomSource random,
            BoundingBox boundingBox,
            net.minecraft.world.level.ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        // Only handle EndCityPiece — other TemplateStructurePiece subclasses
        // (JungleTemplePiece, DesertPyramidPiece, etc.) are handled separately.
        TemplateStructurePiece self = (TemplateStructurePiece) (Object) this;
        String className = self.getClass().getName();
        if (!className.contains("EndCityPiece")) return;

        // Extract template name from the piece
        String templateName = getTemplateName(self);
        if (templateName == null) return;

        float chance = TEMPLATE_CHANCES.getOrDefault(templateName, 0.0f);
        if (chance <= 0) return;

        if (level.isClientSide()) return;

        // Use the piece's actual bounding box for scan (not the chunk box)
        BoundingBox pieceBox = self.getBoundingBox();
        if (pieceBox == null) return;

        // Phase 1b: Collect purpur block positions in piece bounding box
        List<BlockPos> toReplace = new ArrayList<>();

        // Phase 1a: Elytra block 100% replacement (ship template only).
        // The purpur block behind the Elytra ItemFrame is always replaced,
        // bypassing probability and interior-surface checks — mirroring the
        // Nether Fortress lava well pattern (MixinStructurePiece line 192).
        if ("ship".equals(templateName)) {
            handleElytraBlock(toReplace, level, self);
        }
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        long worldSeed = level.getSeed();

        for (int x = pieceBox.minX(); x <= pieceBox.maxX(); x++) {
            for (int y = pieceBox.minY(); y <= pieceBox.maxY(); y++) {
                for (int z = pieceBox.minZ(); z <= pieceBox.maxZ(); z++) {
                    mutablePos.set(x, y, z);
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.is(Blocks.PURPUR_BLOCK)) {
                        if (!isInteriorSurface(level, mutablePos)) continue; // only visible surfaces
                        if (shouldReplace(level, mutablePos, chance)) {
                            toReplace.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }

        if (toReplace.isEmpty()) return;

        // Phase 2: Defer to server tick to avoid blocking chunk generation
        ServerLevel serverLevel = level.getLevel();
        MinecraftServer server = serverLevel.getServer();
        List<BlockPos> injectList = new ArrayList<>(toReplace);
        long seed = worldSeed ^ 0x5EED5EEDL;

        server.execute(() -> {
            int count = 0;
            long lootSeed = seed;
            for (BlockPos pos : injectList) {
                BlockState current = serverLevel.getBlockState(pos);
                if (!current.is(Blocks.PURPUR_BLOCK)) continue;

                BlockState newState = RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get().defaultBlockState();
                serverLevel.setBlock(pos, newState, 2);

                BrushableBlockEntity be = (BrushableBlockEntity) serverLevel.getBlockEntity(pos);
                if (be instanceof RelicBrushableBlockEntity relicBe) {
                    relicBe.relictales$setLootTable(END_CITY_LOOT, lootSeed++);
                    count++;
                }
            }
            if (count > 0) {
                LOG.info("[EC PostProcess] Injected {} suspicious purpur blocks into '{}' (chance={})", count, templateName, chance);
            }
        });
    }

    /**
     * Extract the template name from the piece's templateName field.
     * TemplateStructurePiece stores this as a protected field.
     */
    private static String getTemplateName(TemplateStructurePiece piece) {
        // Use reflection as a last resort — the field is protected but we're
        // outside the class hierarchy.
        try {
            var field = TemplateStructurePiece.class.getDeclaredField("templateName");
            field.setAccessible(true);
            return (String) field.get(piece);
        } catch (Exception e) {
            return null;
        }
    }

    private static StructureTemplate getTemplateField(TemplateStructurePiece piece) {
        try {
            var field = TemplateStructurePiece.class.getDeclaredField("template");
            field.setAccessible(true);
            return (StructureTemplate) field.get(piece);
        } catch (Exception e) {
            return null;
        }
    }

    private static StructurePlaceSettings getPlaceSettings(TemplateStructurePiece piece) {
        try {
            var field = TemplateStructurePiece.class.getDeclaredField("placeSettings");
            field.setAccessible(true);
            return (StructurePlaceSettings) field.get(piece);
        } catch (Exception e) {
            return null;
        }
    }

    private static BlockPos getTemplatePosition(TemplateStructurePiece piece) {
        try {
            var field = TemplateStructurePiece.class.getDeclaredField("templatePosition");
            field.setAccessible(true);
            return (BlockPos) field.get(piece);
        } catch (Exception e) {
            return null;
        }
    }
}

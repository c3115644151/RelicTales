package com.relictales.mixin;

import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StructurePiece.class, remap = false)
public abstract class MixinStructurePiece {

    private static final Logger LOGGER = LoggerFactory.getLogger("relictales");

    // === Loot Keys ===
    private static final ResourceKey<LootTable> LOOT_CRACKED = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_cracked_stone_bricks"));
    private static final ResourceKey<LootTable> LOOT_MOSSY = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_mossy_stone_bricks"));
    private static final ResourceKey<LootTable> LOOT_NETHER = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_nether_bricks"));

    // ===== Stronghold =====

    private static float getReplacementChance(StructurePiece piece, int x, int y, int z) {
        if (piece instanceof StrongholdPieces.Library) return 0.10f;

        if (piece instanceof StrongholdPieces.RoomCrossing room) {
            if (x == 5 && y == 1 && z == 5) return 1.0f;
            if (room instanceof RoomCrossingAccessor acc && acc.relictales$getType() == 2
                    && x == 5 && y == 0 && z == 5) return 1.0f;
            if (y == 0 && x >= 4 && x <= 6 && z >= 4 && z <= 6) return 0.50f;
            return 0.10f;
        }

        if (piece instanceof StrongholdPieces.FiveCrossing) return 0.10f;
        if (piece instanceof StrongholdPieces.ChestCorridor) {
            if (y == 0 && x >= 1 && x <= 3 && z >= 2 && z <= 4) return 1.0f;
            return 0.06f;
        }
        if (piece instanceof StrongholdPieces.PortalRoom) return 0.06f;
        return 0.01f;
    }

    // ===== Nether Fortress =====

    /**
     * Room-specific probability for nether brick → suspicious replacement.
     *
     * Nether fortress is very open/visible compared to strongholds, so
     * per-block probabilities are deliberately low — the sheer number of
     * blocks exposed to air compensates.
     *
     * MonsterThrone (blaze throne)      → 30% — unchanged, small room
     * CastleStalkRoom (blaze+wart room) → 10%
     * StairsRoom / CastleCorridorStairs → 5% each
     * CastleEntrance (lava well)        →  3% (+ 100% on well bottom)
     * CastleCorridorTBalcony            →  4%
     * RoomCrossing / BridgeCrossing     →  3%
     * CastleSmallCorridorCrossing       →  3%
     * Small corridors / turns / BridgeStraight → 1%
     * BridgeEndFiller / StartPiece / universal → 0.2%
     */
    private static float getNetherReplacementChance(StructurePiece piece, int x, int y, int z) {
        if (piece instanceof NetherFortressPieces.MonsterThrone) {
            // MonsterThrone: floor at y=0-1, walls at y>=2. Walls shouldn't outshine the throne.
            return y <= 1 ? 0.30f : 0.10f;
        }
        if (piece instanceof NetherFortressPieces.CastleStalkRoom) {
            // CastleStalkRoom: 13×14×13 room with floor at y=3-4 and tall walls above.
            // Walls at y>=5 span 8 layers and are all air-exposed — at full rate they'd
            // produce far more suspicious blocks than the walkable floor.
            return y <= 4 ? 0.10f : 0.02f;
        }
        if (piece instanceof NetherFortressPieces.StairsRoom) return 0.05f;
        if (piece instanceof NetherFortressPieces.CastleCorridorStairsPiece) return 0.05f;
        if (piece instanceof NetherFortressPieces.CastleCorridorTBalconyPiece) return 0.04f;
        if (piece instanceof NetherFortressPieces.CastleEntrance) return 0.03f;
        if (piece instanceof NetherFortressPieces.RoomCrossing) return 0.03f;
        if (piece instanceof NetherFortressPieces.BridgeCrossing) return 0.03f;
        if (piece instanceof NetherFortressPieces.CastleSmallCorridorCrossingPiece) return 0.03f;
        if (piece instanceof NetherFortressPieces.CastleSmallCorridorPiece) return 0.01f;
        if (piece instanceof NetherFortressPieces.CastleSmallCorridorLeftTurnPiece) return 0.01f;
        if (piece instanceof NetherFortressPieces.CastleSmallCorridorRightTurnPiece) return 0.01f;
        if (piece instanceof NetherFortressPieces.BridgeStraight) return 0.01f;
        return 0.002f; // BridgeEndFiller, StartPiece, and any unlisted piece
    }

    /**
     * Check if at least one directional neighbor is air — avoids replacing
     * buried structural fill (walls, pillar interiors) that players never see.
     */
    private static boolean isAirExposed(WorldGenLevel level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).isAir()) return true;
        }
        return false;
    }

    /**
     * CastleEntrance lava well has a nether brick at local (6, 0, 6) sitting
     * at the very bottom of a 5-deep pit beneath lava. This is the only brick
     * at the lava well that the player can brush — 100% replacement.
     */
    private static boolean isNetherLavaWellBottom(StructurePiece piece, int x, int y, int z) {
        return piece instanceof NetherFortressPieces.CastleEntrance && x == 6 && y == 0 && z == 6;
    }

    // ===== Injection =====

    @Inject(method = "placeBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void relictales$onPlaceBlock(
            WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB, CallbackInfo ci
    ) {
        StructurePiece self = (StructurePiece) (Object) this;
        Class<?> declaring = self.getClass().getDeclaringClass();

        if (declaring == StrongholdPieces.class) {
            handleStronghold(level, state, x, y, z, chunkBB, ci, self);
        } else if (declaring == NetherFortressPieces.class) {
            handleNetherFortress(level, state, x, y, z, chunkBB, ci, self);
        }
    }

    // ===== Stronghold Handler =====

    private void handleStronghold(
            WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB,
            CallbackInfo ci, StructurePiece self
    ) {
        BlockState replacement = null;
        ResourceKey<LootTable> lootKey = null;

        if (state.is(Blocks.CRACKED_STONE_BRICKS)) {
            replacement = RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get().defaultBlockState();
            lootKey = LOOT_CRACKED;
        } else if (state.is(Blocks.MOSSY_STONE_BRICKS)) {
            replacement = RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get().defaultBlockState();
            lootKey = LOOT_MOSSY;
        } else if (state.is(Blocks.STONE_BRICKS) && self instanceof StrongholdPieces.RoomCrossing
                && x == 5 && y == 1 && z == 5) {
            replacement = RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get().defaultBlockState();
            lootKey = LOOT_CRACKED;
        } else {
            return;
        }

        float chance = getReplacementChance(self, x, y, z);
        if (level.getRandom().nextFloat() >= chance) return;

        BlockPos pos = ((StructurePieceInvoker) self).invokeGetWorldPos(x, y, z);
        if (!chunkBB.isInside(pos)) return;

        level.setBlock(pos, replacement, 2);

        LOGGER.info("[RelicTales] SH replaced {} at worldPos={} (room={}, localPos={},{},{})",
                state.getBlock().getName().getString(), pos, self.getClass().getSimpleName(), x, y, z);

        setLootTableDeferred(level, pos, lootKey);
        ci.cancel();
    }

    // ===== Nether Fortress Handler =====

    private void handleNetherFortress(
            WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB,
            CallbackInfo ci, StructurePiece self
    ) {
        // Only intercept NETHER_BRICKS blocks
        if (!state.is(Blocks.NETHER_BRICKS)) return;

        // CastleEntrance lava well bottom block: 100% replacement regardless of air-exposed
        if (isNetherLavaWellBottom(self, x, y, z)) {
            BlockPos pos = ((StructurePieceInvoker) self).invokeGetWorldPos(x, y, z);
            if (!chunkBB.isInside(pos)) return;

            level.setBlock(pos, RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get().defaultBlockState(), 2);
            LOGGER.info("[RelicTales] NF lava-well 100% at worldPos={}", pos);
            setLootTableDeferred(level, pos, LOOT_NETHER);
            ci.cancel();
            return;
        }

        // Chance-based replacement: only for air-exposed blocks (not buried in walls/pillars)
        float chance = getNetherReplacementChance(self, x, y, z);
        if (level.getRandom().nextFloat() >= chance) return;

        BlockPos pos = ((StructurePieceInvoker) self).invokeGetWorldPos(x, y, z);
        if (!chunkBB.isInside(pos)) return;
        if (!isAirExposed(level, pos)) return;

        level.setBlock(pos, RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get().defaultBlockState(), 2);

        LOGGER.info("[RelicTales] NF replaced {} at worldPos={} (room={}, localPos={},{},{})",
                state.getBlock().getName().getString(), pos, self.getClass().getSimpleName(), x, y, z);

        setLootTableDeferred(level, pos, LOOT_NETHER);
        ci.cancel();
    }

    // ===== Shared =====

    private static void setLootTableDeferred(WorldGenLevel level, BlockPos pos, ResourceKey<LootTable> lootKey) {
        ServerLevel serverLevel = level.getLevel();
        if (serverLevel != null) {
            long seed = level.getRandom().nextLong();
            serverLevel.getServer().execute(() -> {
                var be = serverLevel.getBlockEntity(pos);
                if (be instanceof RelicBrushableBlockEntity relicBe) {
                    relicBe.relictales$setLootTable(lootKey, seed);
                }
            });
        }
    }
}

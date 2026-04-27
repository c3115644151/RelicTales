package com.relictales.mixin;

import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
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

    private static final ResourceKey<LootTable> LOOT_CRACKED = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_cracked_stone_bricks"));
    private static final ResourceKey<LootTable> LOOT_MOSSY = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_mossy_stone_bricks"));

    private static float getReplacementChance(StructurePiece piece, int x, int y, int z) {
        // Library: 10% (doubled from 5%)
        if (piece instanceof StrongholdPieces.Library) return 0.10f;

        // RoomCrossing: center pillar 100%, storage center floor 100% (cracked/mossy only),
        // fountain/storage center 3x3 50%, general 10% (doubled from 5%)
        if (piece instanceof StrongholdPieces.RoomCrossing room) {
            // Center pillar base — 100% for all types
            if (x == 5 && y == 1 && z == 5) return 1.0f;

            // Storage room (type 2): center floor at (5,0,5) — 100% for cracked/mossy only
            if (room instanceof RoomCrossingAccessor acc && acc.relictales$getType() == 2
                    && x == 5 && y == 0 && z == 5) {
                return 1.0f;
            }

            // Center 3x3 floor at y=0 — 50% for all RoomCrossing types
            if (y == 0 && x >= 4 && x <= 6 && z >= 4 && z <= 6) return 0.50f;

            // General stronghold room rate: 10% (doubled from 5%)
            return 0.10f;
        }

        // FiveCrossing: 10% (doubled from 5%)
        if (piece instanceof StrongholdPieces.FiveCrossing) return 0.10f;

        // ChestCorridor: center floor 100%, general 6% (doubled from 3%)
        if (piece instanceof StrongholdPieces.ChestCorridor) {
            if (y == 0 && x >= 1 && x <= 3 && z >= 2 && z <= 4) return 1.0f;
            return 0.06f;
        }

        // PortalRoom: 6% (doubled from 3%)
        if (piece instanceof StrongholdPieces.PortalRoom) return 0.06f;

        // All other rooms: 1% (doubled from 0.5%)
        return 0.01f;
    }

    @Inject(method = "placeBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void relictales$onPlaceBlock(
            WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox chunkBB, CallbackInfo ci
    ) {
        StructurePiece self = (StructurePiece) (Object) this;
        // Only apply to stronghold pieces (all inner classes of StrongholdPieces)
        if (self.getClass().getDeclaringClass() != StrongholdPieces.class) return;

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
            // RoomCrossing center pillar → suspicious cracked variant (NOT center floor — regular
            // stone bricks at (5,0,5) should remain unreplaced)
            replacement = RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get().defaultBlockState();
            lootKey = LOOT_CRACKED;
        } else {
            return;
        }

        float chance = getReplacementChance(self, x, y, z);
        if (level.getRandom().nextFloat() >= chance) return;

        BlockPos pos = ((StructurePieceInvoker) self).invokeGetWorldPos(x, y, z);
        if (!chunkBB.isInside(pos)) return;

        // Place our suspicious block (replaces vanilla CRACKED/MOSSY stone bricks)
        level.setBlock(pos, replacement, 2);

        LOGGER.info("[RelicTales] MixinStructurePiece replaced {} at worldPos={} (room={}, localPos={},{},{})",
                state.getBlock().getName().getString(),
                pos,
                self.getClass().getSimpleName(),
                x, y, z);

        // Defer loot table setting to server tick (BE may not be fully initialized during worldgen)
        // NOTE: WorldGenLevel does NOT extend ServerLevel, so we must call level.getLevel()
        ServerLevel serverLevel = level.getLevel();
        if (serverLevel != null) {
            ResourceKey<LootTable> finalLootKey = lootKey;
            long seed = level.getRandom().nextLong();
            serverLevel.getServer().execute(() -> {
                var be = serverLevel.getBlockEntity(pos);
                if (be instanceof RelicBrushableBlockEntity relicBe) {
                    relicBe.relictales$setLootTable(finalLootKey, seed);
                }
            });
        }

        ci.cancel();
    }
}

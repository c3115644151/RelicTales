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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StrongholdPieces.ChestCorridor.class, remap = false)
public abstract class MixinChestCorridor {

    private static final Logger LOGGER = LoggerFactory.getLogger("relictales");
    private static final ResourceKey<LootTable> LOOT_CRACKED = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_cracked_stone_bricks"));

    @Inject(method = "postProcess", at = @At("TAIL"), remap = false)
    private void relictales$injectCenterFloor(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator chunkGenerator,
            RandomSource random,
            BoundingBox boundingBox,
            ChunkPos chunkPos,
            BlockPos referencePos,
            CallbackInfo ci
    ) {
        if (level.isClientSide()) return;

        // Place suspicious block at center floor (local coords: x=2, y=0, z=3)
        // In ChestCorridor (5x5x7), this is the floor block under the central "pillar" area
        StructurePiece self = (StructurePiece) (Object) this;
        BlockPos pos = ((StructurePieceInvoker) self).invokeGetWorldPos(2, 0, 3);
        if (!boundingBox.isInside(pos)) return;

        // Only replace natural terrain (air, stone, deepslate) — don't overwrite player-placed blocks
        BlockState existing = level.getBlockState(pos);
        if (!existing.isAir() && !existing.is(Blocks.STONE) && !existing.is(Blocks.DEEPSLATE)) return;

        BlockState suspicious = RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get().defaultBlockState();
        level.setBlock(pos, suspicious, 2);

        LOGGER.info("[RelicTales] ChestCorridor injected suspicious block at center floor {}", pos);

        if (level instanceof ServerLevel serverLevel) {
            long seed = level.getRandom().nextLong();
            serverLevel.getServer().execute(() -> {
                var be = serverLevel.getBlockEntity(pos);
                if (be instanceof RelicBrushableBlockEntity relicBe) {
                    relicBe.relictales$setLootTable(LOOT_CRACKED, seed);
                }
            });
        }
    }
}

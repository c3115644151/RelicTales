package com.relictales.mixin;

import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.registry.blocks.RelicBlocks;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = DesertPyramidPiece.class, remap = false)
public abstract class MixinDesertPyramidPiece {

    private static final Logger LOG = LoggerFactory.getLogger("relictales");
    private static final ResourceKey<LootTable> DESERT_TEMPLE_LOOT =
            ResourceKey.create(Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_chiseled_sandstone"));

    @Inject(method = "postProcess", at = @At("TAIL"), remap = false)
    private void relictales$injectSuspiciousBlocks(
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

        // Phase 1: Collect CHISELED_SANDSTONE positions deterministically
        List<BlockPos> positions = new ArrayList<>();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        long worldSeed = level.getSeed();

        for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
            for (int y = boundingBox.minY(); y <= boundingBox.maxY(); y++) {
                for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
                    mutablePos.set(x, y, z);
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.is(Blocks.CHISELED_SANDSTONE)) {
                        long posSeed = worldSeed ^ (long) x * 291047L ^ (long) y * 731293L ^ (long) z * 548119L;
                        if ((posSeed & 0xFFFF) < 0x3333) { // ~20% chance
                            positions.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }

        if (positions.isEmpty()) return;

        // Phase 2: Defer to server tick to avoid blocking chunk generation
        ServerLevel serverLevel = level.getLevel();
        MinecraftServer server = serverLevel.getServer();
        List<BlockPos> toInject = new ArrayList<>(positions);
        long seed = worldSeed ^ 0x5EED5EEDL;

        server.execute(() -> {
            int count = 0;
            long lootSeed = seed;
            for (BlockPos pos : toInject) {
                BlockState current = serverLevel.getBlockState(pos);
                if (!current.is(Blocks.CHISELED_SANDSTONE)) continue;

                BlockState newState = RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get().defaultBlockState();
                serverLevel.setBlock(pos, newState, 2);

                BrushableBlockEntity be = (BrushableBlockEntity) serverLevel.getBlockEntity(pos);
                if (be instanceof RelicBrushableBlockEntity relicBe) {
                    relicBe.relictales$setLootTable(DESERT_TEMPLE_LOOT, lootSeed++);
                    count++;
                } else {
                    LOG.warn("[RelicTales] BE at {} is not RelicBrushableBlockEntity (type={})", pos, be.getClass().getSimpleName());
                }
            }
            if (count > 0) {
                LOG.info("[RelicTales] Injected {} suspicious blocks into desert pyramid", count);
            }
        });
    }
}

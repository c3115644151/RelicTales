package com.relictales.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelicBrushableBlockEntity extends BrushableBlockEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("relictales");

    public RelicBrushableBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(worldPosition, blockState);
        LOGGER.info("[RelicBE] Constructor called at pos={}, state={}", worldPosition, blockState);
    }

    public void relictales$setLootTable(ResourceKey<LootTable> lootTable, long seed) {
        // setLootTable is public on BrushableBlockEntity (inherited from BlockEntity)
        this.setLootTable(lootTable, seed);
        LOGGER.info("[RelicBE] setLootTable key={}, seed={}", lootTable, seed);
    }
}

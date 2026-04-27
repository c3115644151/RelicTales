package com.relictales.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RelicsBrushableBlock extends BrushableBlock {

    public RelicsBrushableBlock(
            net.minecraft.world.level.block.Block turnsInto,
            net.minecraft.sounds.SoundEvent brushSound,
            net.minecraft.sounds.SoundEvent brushCompletedSound,
            net.minecraft.world.level.block.state.BlockBehaviour.Properties properties
    ) {
        super(turnsInto, brushSound, brushCompletedSound, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RelicBrushableBlockEntity(pos, state);
    }

    public void relictales$setLootTableOnBe(BlockEntity be,
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTable,
            long seed) {
        if (be instanceof RelicBrushableBlockEntity relicBe) {
            relicBe.relictales$setLootTable(lootTable, seed);
        }
    }
}

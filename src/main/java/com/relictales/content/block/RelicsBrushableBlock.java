package com.relictales.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
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

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Only decay brush count over time — skip the FallingBlockEntity spawn.
        // Vanilla BrushableBlock.tick() spawns a falling entity when the block below is air,
        // which is correct for suspicious sand/gravel but wrong for our stone-like blocks.
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof RelicBrushableBlockEntity brushable) {
            brushable.checkReset(level);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Suppress the falling-dust particles when the block is suspended in air.
        // Vanilla BrushableBlock.animateTick() shows FALLING_DUST particles below the block
        // when the space beneath is empty, which looks wrong for stone-like blocks.
    }

    public void relictales$setLootTableOnBe(BlockEntity be,
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTable,
            long seed) {
        if (be instanceof RelicBrushableBlockEntity relicBe) {
            relicBe.relictales$setLootTable(lootTable, seed);
        }
    }
}

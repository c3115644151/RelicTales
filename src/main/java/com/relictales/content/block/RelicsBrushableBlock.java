package com.relictales.content.block;

import com.relictales.RelicTales;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

public class RelicsBrushableBlock extends BrushableBlock {

    public static BlockEntityType<RelicBrushableBlockEntity> BLOCK_ENTITY_TYPE = null;

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
        return new RelicBrushableBlockEntity(BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RelicBrushableBlockEntity relicBe) {
                ResourceKey<LootTable> lootKey = ResourceKey.create(
                        Registries.LOOT_TABLE,
                        net.minecraft.resources.Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, "blocks/suspicious_mossy_stone_bricks")
                );
                relicBe.setLootTable(lootKey, 0L);
            }
        }
    }
}
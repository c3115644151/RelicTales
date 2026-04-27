package com.relictales.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public class RelicBrushableBlockItem extends BlockItem {

    private final ResourceKey<LootTable> lootTableKey;

    public RelicBrushableBlockItem(Block block, Properties properties, ResourceKey<LootTable> lootTableKey) {
        super(block, properties);
        this.lootTableKey = lootTableKey;
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result.consumesAction() && !ctx.getLevel().isClientSide()) {
            applyLootTableNbt(ctx);
        }
        return result;
    }

    private void applyLootTableNbt(BlockPlaceContext ctx) {
        var be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
        if (be instanceof RelicBrushableBlockEntity relicBe) {
            relicBe.relictales$setLootTable(this.lootTableKey, ctx.getClickedPos().asLong());
        }
    }
}

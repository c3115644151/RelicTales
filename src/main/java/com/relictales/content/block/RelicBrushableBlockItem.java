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

    public RelicBrushableBlockItem(Block block, Properties properties) {
        super(block, properties);
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
            ResourceKey<LootTable> lootKey = ResourceKey.create(
                    Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("relictales", "blocks/suspicious_mossy_cobblestone"));
            relicBe.relictales$setLootTable(lootKey, ctx.getClickedPos().asLong());
        }
    }
}

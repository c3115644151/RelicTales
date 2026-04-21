package com.relictales.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public class RelicBrushableBlockItem extends BlockItem {

    public static final String LOOT_TABLE_KEY = "LootTable";
    public static final String LOOT_TABLE_SEED_KEY = "LootTableSeed";

    private String lootTableId;

    public RelicBrushableBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public void setLootTable(String lootTableId) {
        this.lootTableId = lootTableId;
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
        String lootTableStr = this.lootTableId;

        var customData = ctx.getItemInHand()
                .get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty()) {
            CompoundTag nbt = customData.copyTag();
            String nbtTable = nbt.getString(LOOT_TABLE_KEY).orElse("");
            if (!nbtTable.isEmpty()) {
                lootTableStr = nbtTable;
            }
        }

        if (lootTableStr == null || lootTableStr.isEmpty()) return;

        ResourceKey<LootTable> lootKey = ResourceKey.create(
                Registries.LOOT_TABLE, Identifier.parse(lootTableStr));

        var be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
        if (be instanceof RelicBrushableBlockEntity relicBe) {
            relicBe.setLootTable(lootKey, 0L);
        }
    }
}

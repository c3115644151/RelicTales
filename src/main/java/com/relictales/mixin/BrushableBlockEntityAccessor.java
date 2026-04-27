package com.relictales.mixin;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BrushableBlockEntity.class, remap = false)
public interface BrushableBlockEntityAccessor {

    @Accessor(value = "lootTable", remap = false)
    ResourceKey<LootTable> getLootTableKeyForAccessor();

    @Accessor(value = "lootTable", remap = false)
    void setLootTable(ResourceKey<LootTable> lootTable);

    @Accessor(value = "lootTableSeed", remap = false)
    long getLootTableSeed();

    @Accessor(value = "brushCount", remap = false)
    int getBrushCount();

    @Accessor(value = "brushCount", remap = false)
    void setBrushCount(int count);

    @Accessor(value = "item", remap = false)
    ItemStack getBrushedItem();

    @Accessor(value = "item", remap = false)
    void setBrushedItem(ItemStack item);

    @Accessor(value = "hitDirection", remap = false)
    void setHitDirection(Direction direction);
}
package com.relictales.registry.blocks;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockEntity;
import com.relictales.content.block.RelicBrushableBlockItem;
import com.relictales.content.block.RelicsBrushableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicTales.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_MOSSY_STONE_BRICKS = BLOCKS.register(
            "suspicious_mossy_stone_bricks",
            id -> {
                var props = net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy(Blocks.SUSPICIOUS_SAND)
                        .setId(ResourceKey.create(Registries.BLOCK, id));
                var block = new RelicsBrushableBlock(
                        Blocks.MOSSY_STONE_BRICKS,
                        SoundEvents.BRUSH_GENERIC,
                        SoundEvents.BRUSH_GRAVEL_COMPLETED,
                        props
                );

                @SuppressWarnings("unchecked")
                BlockEntityType<RelicBrushableBlockEntity>[] betHolder = new BlockEntityType[1];
                betHolder[0] = new BlockEntityType<>(
                        (pos, state) -> new RelicBrushableBlockEntity(betHolder[0], pos, state),
                        java.util.Set.of(block)
                );
                com.relictales.registry.blockentities.RelicBlockEntities.setBlockEntityType(block, betHolder[0]);
                return block;
            }
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_MOSSY_STONE_BRICKS_ITEM;

    public static void init() {
        SUSPICIOUS_MOSSY_STONE_BRICKS_ITEM = ITEMS.register(
                "suspicious_mossy_stone_bricks",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_MOSSY_STONE_BRICKS.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))
                )
        );
    }

    public static DeferredRegister.Blocks getBlocks() { return BLOCKS; }
    public static DeferredRegister.Items getItems()   { return ITEMS; }
}
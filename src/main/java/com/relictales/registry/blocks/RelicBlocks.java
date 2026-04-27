package com.relictales.registry.blocks;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockItem;
import com.relictales.content.block.RelicsBrushableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicTales.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_MOSSY_COBBLESTONE = BLOCKS.register(
            "suspicious_mossy_cobblestone",
            id -> new RelicsBrushableBlock(
                    net.minecraft.world.level.block.Blocks.MOSSY_COBBLESTONE,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    net.minecraft.sounds.SoundEvents.BRUSH_GRAVEL_COMPLETED,
                    net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.SUSPICIOUS_SAND)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_MOSSY_COBBLESTONE_ITEM;

    public static void init() {
        SUSPICIOUS_MOSSY_COBBLESTONE_ITEM = ITEMS.register(
                "suspicious_mossy_cobblestone",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_MOSSY_COBBLESTONE.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))
                )
        );
    }

    public static DeferredRegister.Blocks getBlocks() { return BLOCKS; }
    public static DeferredRegister.Items getItems()   { return ITEMS; }
}

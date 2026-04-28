package com.relictales.registry;

import com.relictales.registry.blocks.RelicBlocks;
import com.relictales.registry.items.RelicItems;
import com.relictales.registry.processors.ModStructureProcessors;
import com.relictales.test.RelicBrushInteractionTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

public class RelicRegisters {

    public static void init(IEventBus bus) {
        // Register blocks
        RelicBlocks.getBlocks().register(bus);
        RelicBlocks.init();
        RelicBlocks.getItems().register(bus);

        RelicItems.init();
        RelicItems.getItems().register(bus);
        CreativeTab.CREATIVE_TABS.register(bus);
        ModStructureProcessors.PROCESSORS.register(bus);

        // Without this, BrushableBlockEntity's constructor throws:
        //   "Invalid block entity minecraft:brushable_block state at ... got relictales:suspicious_mossy_cobblestone"
        // Because BlockEntity.isValidBlockState() checks BET.isValid(blockState) which
        // verifies the block is in BET.validBlocks. Vanilla BRUSHABLE_BLOCK only has
        // SUSPICIOUS_SAND and SUSPICIOUS_GRAVEL — our block must be added here.
        bus.addListener(BlockEntityTypeAddBlocksEvent.class, RelicRegisters::onBlockEntityTypeAddBlocks);

        // Register game tests (NeoForge 26.1: DeferredRegister dispatches to RegisterGameTestsEvent automatically)
        RelicBrushInteractionTest.register(bus);
    }

    private static void onBlockEntityTypeAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(net.minecraft.world.level.block.entity.BlockEntityType.BRUSHABLE_BLOCK,
                RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE.get(),
                RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE.get(),
                RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS.get(),
                RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS.get(),
                RelicBlocks.SUSPICIOUS_NETHER_BRICKS.get(),
                RelicBlocks.SUSPICIOUS_PURPUR_BLOCK.get());
    }
}



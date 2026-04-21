package com.relictales.registry;

import com.relictales.registry.blockentities.RelicBlockEntities;
import com.relictales.registry.blocks.RelicBlocks;
import com.relictales.registry.items.RelicItems;
import com.relictales.registry.processors.ModStructureProcessors;
import net.neoforged.bus.api.IEventBus;

public class RelicRegisters {

    public static void init(IEventBus bus) {
        // 1. Trigger Block registration (factories run during class loading).
        //    Inside RelicBlocks factory: BET is created inline and injected via setBlockEntityType().
        RelicBlocks.getBlocks();

        // 2. Register blocks/items with the event bus
        RelicBlocks.getBlocks().register(bus);
        RelicBlocks.init();
        RelicBlocks.getItems().register(bus);

        // 3. Register other content
        RelicItems.init();
        RelicItems.getItems().register(bus);
        CreativeTab.CREATIVE_TABS.register(bus);
        ModStructureProcessors.PROCESSORS.register(bus);

        // 4. Register BlockEntityType
        RelicBlockEntities.getBlockEntityTypes().register(bus);
    }
}

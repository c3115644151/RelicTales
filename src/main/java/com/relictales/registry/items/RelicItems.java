package com.relictales.registry.items;

import com.relictales.RelicTales;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    public static void init() {
        // Items will be registered here in future milestones
    }

    public static DeferredRegister.Items getItems() { return ITEMS; }
}

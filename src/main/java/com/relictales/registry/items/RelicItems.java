package com.relictales.registry.items;

import com.relictales.RelicTales;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    public static DeferredItem<Item> JUNGLE_HUNTER_FEATHER;
    public static DeferredItem<Item> SUN_GOD_BADGE;

    public static void init() {
        JUNGLE_HUNTER_FEATHER = ITEMS.registerSimpleItem("jungle_hunter_feather");
        SUN_GOD_BADGE = ITEMS.registerSimpleItem("sun_god_badge");
    }

    public static DeferredRegister.Items getItems() { return ITEMS; }
}

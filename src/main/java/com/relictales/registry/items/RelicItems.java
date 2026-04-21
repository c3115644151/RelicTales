package com.relictales.registry.items;

import com.relictales.RelicTales;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicItems {

    // 每个注册类持有自己的 DeferredRegister，互不依赖
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    // 丛林猎羽符 - 使用 registerSimpleItem，内部自动处理 setId
    public static DeferredItem<Item> JUNGLE_HUNTER_FEATHER;

    public static void init() {
        JUNGLE_HUNTER_FEATHER = ITEMS.registerSimpleItem("jungle_hunter_feather");
    }

    public static DeferredRegister.Items getItems() { return ITEMS; }
}

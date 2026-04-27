package com.relictales.registry;

import com.relictales.RelicTales;
import com.relictales.registry.blocks.RelicBlocks;
import com.relictales.registry.items.RelicItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RelicTales.MOD_ID);

    public static final Supplier<CreativeModeTab> RELICTALES_TAB = CREATIVE_TABS.register(
            "relictales_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + RelicTales.MOD_ID))
                    .icon(() -> new ItemStack(RelicItems.JUNGLE_HUNTER_FEATHER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(RelicItems.JUNGLE_HUNTER_FEATHER);
                        output.accept(RelicItems.SUN_GOD_BADGE);
                        output.accept(RelicBlocks.SUSPICIOUS_MOSSY_COBBLESTONE_ITEM);
                        output.accept(RelicBlocks.SUSPICIOUS_CHISELED_SANDSTONE_ITEM);
                        output.accept(RelicBlocks.SUSPICIOUS_CRACKED_STONE_BRICKS_ITEM);
                        output.accept(RelicBlocks.SUSPICIOUS_MOSSY_STONE_BRICKS_ITEM);
                    })
                    .build()
    );
}

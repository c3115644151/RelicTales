package com.relictales.registry;

import com.relictales.RelicTales;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicRegisters {

    // Core registries - used in M1
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, RelicTales.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, RelicTales.MOD_ID);

    // Entity registries (M5)
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RelicTales.MOD_ID);

    // Block entity registries (M2, M3)
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RelicTales.MOD_ID);

    // Sound registries (M2)
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, RelicTales.MOD_ID);

    // Mob effect registries (M3 curse)
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, RelicTales.MOD_ID);

    // Potion registries (M3)
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, RelicTales.MOD_ID);

    // Creative tab registry
    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, RelicTales.MOD_ID);

    // Enchantment registry (future - uncomment when needed)
    // public static final DeferredRegister<net.minecraft.world.item.enchantment.Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, RelicTales.MOD_ID);

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        SOUNDS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MOB_EFFECTS.register(bus);
        POTIONS.register(bus);
        CREATIVE_TABS.register(bus);
        // ENCHANTMENTS.register(bus);
    }
}

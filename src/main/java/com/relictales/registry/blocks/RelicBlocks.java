package com.relictales.registry.blocks;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicBrushableBlockItem;
import com.relictales.content.block.RelicsBrushableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RelicTales.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RelicTales.MOD_ID);

    private static ResourceKey<LootTable> lootKey(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE,
                Identifier.fromNamespaceAndPath(RelicTales.MOD_ID, path));
    }

    private static net.minecraft.sounds.SoundEvent breakSoundOf(net.minecraft.world.level.block.Block block) {
        return block.defaultBlockState().getSoundType().getBreakSound();
    }

    // === Jungle Temple ===
    public static final ResourceKey<LootTable> LOOT_SUSPICIOUS_MOSSY_COBBLESTONE =
            lootKey("blocks/suspicious_mossy_cobblestone");

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_MOSSY_COBBLESTONE = BLOCKS.register(
            "suspicious_mossy_cobblestone",
            id -> new RelicsBrushableBlock(
                    Blocks.MOSSY_COBBLESTONE,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    breakSoundOf(Blocks.MOSSY_COBBLESTONE),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_MOSSY_COBBLESTONE_ITEM;

    // === Desert Temple ===
    public static final ResourceKey<LootTable> LOOT_SUSPICIOUS_CHISELED_SANDSTONE =
            lootKey("blocks/suspicious_chiseled_sandstone");

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_CHISELED_SANDSTONE = BLOCKS.register(
            "suspicious_chiseled_sandstone",
            id -> new RelicsBrushableBlock(
                    Blocks.CHISELED_SANDSTONE,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    breakSoundOf(Blocks.CHISELED_SANDSTONE),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_SANDSTONE)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_CHISELED_SANDSTONE_ITEM;

    // === Stronghold ===
    public static final ResourceKey<LootTable> LOOT_SUSPICIOUS_CRACKED_STONE_BRICKS =
            lootKey("blocks/suspicious_cracked_stone_bricks");

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_CRACKED_STONE_BRICKS = BLOCKS.register(
            "suspicious_cracked_stone_bricks",
            id -> new RelicsBrushableBlock(
                    Blocks.CRACKED_STONE_BRICKS,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    breakSoundOf(Blocks.CRACKED_STONE_BRICKS),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static final ResourceKey<LootTable> LOOT_SUSPICIOUS_MOSSY_STONE_BRICKS =
            lootKey("blocks/suspicious_mossy_stone_bricks");

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_MOSSY_STONE_BRICKS = BLOCKS.register(
            "suspicious_mossy_stone_bricks",
            id -> new RelicsBrushableBlock(
                    Blocks.MOSSY_STONE_BRICKS,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    breakSoundOf(Blocks.MOSSY_STONE_BRICKS),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_STONE_BRICKS)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_CRACKED_STONE_BRICKS_ITEM;
    public static DeferredItem<BlockItem> SUSPICIOUS_MOSSY_STONE_BRICKS_ITEM;

    // === Nether Fortress ===
    public static final ResourceKey<LootTable> LOOT_SUSPICIOUS_NETHER_BRICKS =
            lootKey("blocks/suspicious_nether_bricks");

    public static final DeferredBlock<RelicsBrushableBlock> SUSPICIOUS_NETHER_BRICKS = BLOCKS.register(
            "suspicious_nether_bricks",
            id -> new RelicsBrushableBlock(
                    Blocks.NETHER_BRICKS,
                    net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                    breakSoundOf(Blocks.NETHER_BRICKS),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)
                            .setId(ResourceKey.create(Registries.BLOCK, id))
            )
    );

    public static DeferredItem<BlockItem> SUSPICIOUS_NETHER_BRICKS_ITEM;

    public static void init() {
        SUSPICIOUS_MOSSY_COBBLESTONE_ITEM = ITEMS.register(
                "suspicious_mossy_cobblestone",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_MOSSY_COBBLESTONE.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)),
                        LOOT_SUSPICIOUS_MOSSY_COBBLESTONE
                )
        );

        SUSPICIOUS_CHISELED_SANDSTONE_ITEM = ITEMS.register(
                "suspicious_chiseled_sandstone",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_CHISELED_SANDSTONE.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)),
                        LOOT_SUSPICIOUS_CHISELED_SANDSTONE
                )
        );

        SUSPICIOUS_CRACKED_STONE_BRICKS_ITEM = ITEMS.register(
                "suspicious_cracked_stone_bricks",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_CRACKED_STONE_BRICKS.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)),
                        LOOT_SUSPICIOUS_CRACKED_STONE_BRICKS
                )
        );

        SUSPICIOUS_MOSSY_STONE_BRICKS_ITEM = ITEMS.register(
                "suspicious_mossy_stone_bricks",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_MOSSY_STONE_BRICKS.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)),
                        LOOT_SUSPICIOUS_MOSSY_STONE_BRICKS
                )
        );

        SUSPICIOUS_NETHER_BRICKS_ITEM = ITEMS.register(
                "suspicious_nether_bricks",
                id -> new RelicBrushableBlockItem(
                        SUSPICIOUS_NETHER_BRICKS.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)),
                        LOOT_SUSPICIOUS_NETHER_BRICKS
                )
        );
    }

    public static DeferredRegister.Blocks getBlocks() { return BLOCKS; }
    public static DeferredRegister.Items getItems()   { return ITEMS; }
}

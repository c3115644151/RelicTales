package com.relictales.registry.blockentities;

import com.relictales.RelicTales;
import com.relictales.content.block.RelicsBrushableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RelicBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RelicTales.MOD_ID);

    /** BlockEntityType for suspicious bricks. Set by setBlockEntityType(). */
    public static BlockEntityType<com.relictales.content.block.RelicBrushableBlockEntity> BLOCK_ENTITY_TYPE;

    /**
     * Registered so the type has a key in the registry.
     * Factory reads BLOCK_ENTITY_TYPE which is set by RelicBlocks during its own factory.
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.relictales.content.block.RelicBrushableBlockEntity>> SUSPICIOUS_BRICK_BET =
            BLOCK_ENTITY_TYPES.register("suspicious_brick_be", () -> BLOCK_ENTITY_TYPE);

    public static DeferredRegister<BlockEntityType<?>> getBlockEntityTypes() {
        return BLOCK_ENTITY_TYPES;
    }

    /**
     * Called by RelicBlocks factory. Sets up the BET and injects it into the block.
     *
     * @param blockInstance the freshly constructed block (passed to avoid .get() recursion)
     * @param type the BlockEntityType created inline in the block factory
     */
    public static void setBlockEntityType(
            net.minecraft.world.level.block.Block blockInstance,
            BlockEntityType<com.relictales.content.block.RelicBrushableBlockEntity> type
    ) {
        BLOCK_ENTITY_TYPE = type;
        RelicsBrushableBlock.BLOCK_ENTITY_TYPE = type;
    }
}

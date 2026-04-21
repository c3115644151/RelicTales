package com.relictales.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RelicBrushableBlockEntity extends BlockEntity {

    private static final int REQUIRED_BRUSHES = 10;

    private @Nullable ResourceKey<LootTable> lootTable;
    private long lootTableSeed;
    private int brushCount;
    private long brushCountResetsAtTick;
    private long coolDownEndsAtTick;
    private ItemStack item = ItemStack.EMPTY;
    private @Nullable Direction hitDirection;

    public RelicBrushableBlockEntity(
            BlockEntityType<RelicBrushableBlockEntity> type,
            BlockPos worldPosition,
            BlockState blockState
    ) {
        super(type, worldPosition, blockState);
    }

    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable, long seed) {
        this.lootTable = lootTable;
        this.lootTableSeed = seed;
    }

    public boolean brush(long gameTime, ServerLevel level, Player player, ItemStack brushItem) {
        Direction direction = Direction.DOWN;
        if (this.brushCount == 0) {
            this.hitDirection = direction;
        }

        if (this.brushCount > 0 && this.hitDirection != direction) {
            return false;
        }

        if (this.coolDownEndsAtTick > gameTime) {
            return false;
        }

        this.brushCountResetsAtTick = gameTime + BrushableBlock.TICK_DELAY;
        this.coolDownEndsAtTick = gameTime + 2L;

        if (this.item.isEmpty()) {
            if (this.brushCount > 0) {
                this.checkReset(level);
            }
            return false;
        }

        this.brushCount++;
        float pitch = 0.9F + getLevel().getRandom().nextFloat() * 0.4F;
        level.playSound(player, this.getBlockPos(),
                net.minecraft.sounds.SoundEvents.BRUSH_GENERIC,
                net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, pitch);

        if (this.brushCount >= REQUIRED_BRUSHES) {
            this.brushCount = 0;
            this.spawnLoot(level, player);

            BlockState currentState = getLevel().getBlockState(getBlockPos());
            BlockState turnsInto = Blocks.MOSSY_STONE_BRICKS.withPropertiesOf(currentState);
            level.setBlockAndUpdate(getBlockPos(), turnsInto);
            level.playSound(player, this.getBlockPos(),
                    net.minecraft.sounds.SoundEvents.BRUSH_GRAVEL_COMPLETED,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        return false;
    }

    public void checkReset(ServerLevel level) {
        if (this.brushCount > 0 && this.brushCountResetsAtTick < level.getGameTime()) {
            this.brushCount = 0;
        }
    }

    private void spawnLoot(ServerLevel level, @Nullable LivingEntity player) {
        if (this.lootTable == null) return;

        var lootRegistry = level.getServer().registryAccess().lookupOrThrow(Registries.LOOT_TABLE);
        var lootTableHolder = lootRegistry.get(this.lootTable);
        if (lootTableHolder.isEmpty()) return;
        LootTable table = lootTableHolder.get().value();

        Vec3 origin = Vec3.atCenterOf(getBlockPos());
        LootParams.Builder paramsBuilder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.BLOCK_STATE, getBlockState());

        if (player != null) {
            paramsBuilder.withParameter(LootContextParams.THIS_ENTITY, player);
        }

        LootParams params = paramsBuilder.create(LootContextParamSets.EMPTY);
        table.getRandomItems(params, this.lootTableSeed, lootItem -> {
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    getBlockPos().getX() + 0.5,
                    getBlockPos().getY() + 0.5,
                    getBlockPos().getZ() + 0.5,
                    lootItem
            );
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        });
    }
}

package com.relictales.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BrushableBlockEntity.class, remap = false)
public interface BrushableBlockEntityInvoker {

    @Invoker(value = "brushingCompleted", remap = false)
    void invokeBrushingCompleted(ServerLevel level, LivingEntity user, ItemStack brush);

    @Invoker(value = "dropContent", remap = false)
    void invokeDropContent(ServerLevel level, LivingEntity user, ItemStack brush);
}

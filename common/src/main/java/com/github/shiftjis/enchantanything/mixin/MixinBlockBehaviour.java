package com.github.shiftjis.enchantanything.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {

    @Unique
    private Player player;

    @Inject(method = "getDestroyProgress", at = @At("HEAD"))
    public void getDestroyProgressHead(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        this.player = player;
    }

    @Redirect(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    public float getDestroyProgress(BlockState instance, BlockGetter blockGetter, BlockPos blockPos) {
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, player);
        float destroySpeed = instance.getDestroySpeed(blockGetter, blockPos);
        if (enchantmentLevel > 100) {
            destroySpeed = Math.max(destroySpeed, 100);
        }

        return destroySpeed;
    }
}

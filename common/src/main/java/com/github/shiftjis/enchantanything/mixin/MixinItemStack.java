package com.github.shiftjis.enchantanything.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    public void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    public void getDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        float currentValue = cir.getReturnValue();
        if (currentValue == 1.0F) {
            // reference: net/minecraft/world/entity/player/Player.java.getDestroySpeed
            cir.setReturnValue(1.01F);
        }
    }
}

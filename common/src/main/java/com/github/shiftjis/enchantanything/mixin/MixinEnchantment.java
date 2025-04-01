package com.github.shiftjis.enchantanything.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {
    @Inject(method = "isCompatibleWith", at = @At("HEAD"), cancellable = true)
    public final void isCompatibleWith(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    public void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "isTreasureOnly", at = @At("HEAD"), cancellable = true)
    public void isTreasureOnly(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

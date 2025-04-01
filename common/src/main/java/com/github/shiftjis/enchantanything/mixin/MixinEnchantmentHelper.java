package com.github.shiftjis.enchantanything.mixin;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
    @Inject(method = "getEnchantmentCost", at = @At("HEAD"), cancellable = true)
    private static void getEnchantmentCost(RandomSource random, int enchantNum, int power, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int minCost = random.nextInt(8) + 1 + (power >> 1) + random.nextInt(power + 1);
        minCost = enchantNum == 0 ? Math.max(minCost / 3, 1) : enchantNum == 1 ? minCost * 2 / 3 + 1 : Math.max(minCost, power * 2);
        cir.setReturnValue(minCost + (stack.getEnchantmentTags().size() * 3));
    }

    @Inject(method = "getAvailableEnchantmentResults", at = @At("HEAD"), cancellable = true)
    private static void getAvailableEnchantmentResults(int level, ItemStack stack, boolean allowTreasure, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> enchantmentInstances = Lists.newArrayList();
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                enchantmentInstances.add(new EnchantmentInstance(enchantment, i));
            }
        }
        cir.setReturnValue(enchantmentInstances);
    }

    @Inject(method = "getItemEnchantmentLevel", at = @At("HEAD"), cancellable = true)
    private static void getItemEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.isEmpty()) {
            cir.setReturnValue(0);
            return;
        }

        ResourceLocation enchantmentId = getEnchantmentId(enchantment);
        ListTag enchantmentTags = stack.getEnchantmentTags();

        int totalLevel = 0;
        for (int i = 0; i < enchantmentTags.size(); ++i) {
            CompoundTag compoundtag = enchantmentTags.getCompound(i);
            ResourceLocation tagEnchantmentId = EnchantmentHelper.getEnchantmentId(compoundtag);
            if (tagEnchantmentId != null && tagEnchantmentId.equals(enchantmentId)) {
                totalLevel += EnchantmentHelper.getEnchantmentLevel(compoundtag);
            }
        }

        cir.setReturnValue(totalLevel);
    }
}

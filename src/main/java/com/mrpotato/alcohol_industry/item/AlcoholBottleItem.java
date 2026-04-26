package com.mrpotato.alcohol_industry.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class AlcoholBottleItem extends Item {
    
    private final Supplier<List<MobEffectInstance>> effects;

    public AlcoholBottleItem(Properties properties, Supplier<List<MobEffectInstance>> effects) {
        super(properties);
        this.effects = effects;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        
        if (!level.isClientSide && entity instanceof Player player) {
            for (MobEffectInstance effect : effects.get()) {
                player.addEffect(new MobEffectInstance(effect));
            }
        }
        
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (result.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(bottle)) {
                player.drop(bottle, false);
            }
        }
        return result;
    }
}

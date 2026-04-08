package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class SoulSiphonItem extends SwordItem {
    public SoulSiphonItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // In NeoForge/Mojang mappings, postHit is called hurtEnemy.
        // We check if target is dead. Note: isDeadOrDying() is often safer,
        // but here we follow your logic of target.isRemoved() or health check if needed.
        if (!target.isAlive() && !target.level().isClientSide()) {
            if (target.getType().is(ModTags.Entities.SOULLESS)) {
                return super.hurtEnemy(stack, target, attacker);
            }

            float maxHealth = target.getMaxHealth();
            int fragmentsToDrop = 1 + (int)(maxHealth / 20.0f);

            ItemEntity soulDrop = new ItemEntity(
                    target.level(), target.getX(), target.getY(), target.getZ(),
                    new ItemStack(ModItems.SOUL_FRAGMENT.get(), fragmentsToDrop)
            );
            target.level().addFreshEntity(soulDrop);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        if (!level.isClientSide()) {
            // Check for the soul fragment in the player inventory
            // NeoForge/Vanilla: search for the item stack
            boolean hasFragment = false;
            int fragmentSlot = -1;

            for (int i = 0; i < user.getInventory().getContainerSize(); i++) {
                ItemStack invStack = user.getInventory().getItem(i);
                if (invStack.is(ModItems.SOUL_FRAGMENT.get())) {
                    hasFragment = true;
                    fragmentSlot = i;
                    break;
                }
            }

            if (hasFragment) {
                user.getInventory().getItem(fragmentSlot).shrink(1);
                // addEffect is the NeoForge equivalent of addStatusEffect
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100)); // Strength
                user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1)); // Speed II

                return InteractionResultHolder.consume(itemStack);
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
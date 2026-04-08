package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonBowItem extends BowItem {
    public DragonBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        // Multiply velocity by 2 to achieve the custom projectile speed from your Fabric version
        super.shoot(level, shooter, hand, weapon, projectiles, velocity * 2.0F, inaccuracy, isCrit, target);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 30;
    }
}
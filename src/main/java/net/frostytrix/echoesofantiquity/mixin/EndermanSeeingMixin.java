package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public class EndermanSeeingMixin {

    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    private void isLookingAtMeObsidianGoggles(Player player, CallbackInfoReturnable<Boolean> cir) {
        // Index 3 is the helmet slot
        ItemStack item = player.getInventory().armor.get(3);
        if (item.is(ModItems.OBSIDIAN_GOGGLES.get())) {
            cir.setReturnValue(false);
        }
    }
}
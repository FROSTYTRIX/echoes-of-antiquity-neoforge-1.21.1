package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class VoidChainmailChestplateItem extends ModArmorItem {

    public VoidChainmailChestplateItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide() && entity instanceof Player player) {

            if (player.getInventory().getArmor(2) != stack) {
                return;
            }

            if (hasFullSuitOfArmorOn(player) && hasCorrectArmorOn(ModArmorMaterials.VOID_CHAINMAIL, player)) {

                CustomData nbtComponent = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag nbt = nbtComponent.copyTag();

                if (player.onGround()) {
                    double safeX = Math.floor(player.getX()) + 0.5;
                    double safeY = player.getY();
                    double safeZ = Math.floor(player.getZ()) + 0.5;

                    boolean shouldUpdate = true;
                    if (nbt.contains("last_safe_x")) {
                        double lastX = nbt.getDouble("last_safe_x");
                        double lastY = nbt.getDouble("last_safe_y");
                        double lastZ = nbt.getDouble("last_safe_z");

                        if (Math.abs(lastX - safeX) < 0.1 && Math.abs(lastY - safeY) < 0.5 && Math.abs(lastZ - safeZ) < 0.1) {
                            shouldUpdate = false;
                        }
                    }

                    if (shouldUpdate) {
                        nbt.putDouble("last_safe_x", safeX);
                        nbt.putDouble("last_safe_y", safeY);
                        nbt.putDouble("last_safe_z", safeZ);
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                    }
                }

                if (player.getY() < level.getMinBuildHeight() - 4) { // Replaces the hard-coded -64
                    if (nbt.contains("last_safe_x")) {
                        double safeX = nbt.getDouble("last_safe_x");
                        double safeY = nbt.getDouble("last_safe_y");
                        double safeZ = nbt.getDouble("last_safe_z");

                        player.teleportTo(safeX, safeY + 0.2, safeZ);
                        player.setDeltaMovement(0, 0, 0);
                        player.hasImpulse = true;
                        player.fallDistance = 0.0f;

                        level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

                        for (int i = 0; i < 4; i++) {
                            ItemStack armorPiece = player.getInventory().getArmor(i);

                            if (!armorPiece.isEmpty()) {
                                int maxDamage = armorPiece.getMaxDamage();
                                int damageAmount = maxDamage / 3;
                                int currentDamage = armorPiece.getOrDefault(DataComponents.DAMAGE, 0);

                                if (currentDamage + damageAmount >= maxDamage) {
                                    armorPiece.shrink(1);
                                    level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                                } else {
                                    armorPiece.set(DataComponents.DAMAGE, currentDamage + damageAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
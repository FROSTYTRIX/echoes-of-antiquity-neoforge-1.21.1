package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.custom.VoidPedestalBlock;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidPedestalBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                // This is CRITICAL: It tells the server to send the new inventory to all nearby clients
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private float rotation = 0;
    public static final int NO_TP_RADIUS = 20;

    public VoidPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VOID_PEDESTAL_BE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean isActivated() {
        return this.inventory.getStackInSlot(0).is(Items.ENDER_EYE);
    }

    public float getRenderingRotation(BlockPos pos, Level level) {
        Player player = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0, false);

        if (player != null && isActivated()) {
            double d = player.getX() - (pos.getX() + 0.5);
            double e = player.getZ() - (pos.getZ() + 0.5);
            rotation = (float) Mth.atan2(e, d);
            return -((rotation + Mth.HALF_PI) * 180f) / Mth.PI;
        } else {
            rotation += 0.5f;
            if (rotation >= 360) {
                rotation = 0;
            }
        }
        return rotation;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
    }

    // --- SYNCING LOGIC ---

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Creates the packet that sends the BE data to the client
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        // Creates the tag used when the chunk first loads for the client
        return saveWithoutMetadata(registries);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VoidPedestalBlockEntity be) {
        if (level.isClientSide()) return;

        boolean hasItem = be.inventory.getStackInSlot(0).is(Items.ENDER_EYE);

        if (state.getValue(VoidPedestalBlock.ACTIVE) != hasItem) {
            level.setBlock(pos, state.setValue(VoidPedestalBlock.ACTIVE, hasItem), 3);
        }

        if (hasItem) {
            AABB area = new AABB(pos).inflate(NO_TP_RADIUS);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive);

            for (LivingEntity entity : entities) {
                entity.addTag("void_pedestal_suppressed");
            }
        }
    }
}
package net.frostytrix.echoesofantiquity.screen.custom;

import net.frostytrix.echoesofantiquity.block.entity.custom.SieveBlockEntity;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SieveScreenHandler extends AbstractContainerMenu {
    private final SieveBlockEntity blockEntity;
    private final ContainerData data;

    // Constructor used by NeoForge to initialize on the client
    public SieveScreenHandler(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public SieveScreenHandler(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModScreenHandlers.SIEVE_SCREEN_HANDLER.get(), id);
        checkContainerDataCount(data, 2); // 2 pieces of data: progress and maxProgress
        this.blockEntity = (SieveBlockEntity) entity;
        this.data = data;

        IItemHandler inventory = blockEntity.getInventory();

        // Input Slot
        this.addSlot(new SlotItemHandler(inventory, 0, 34, 17));

        // Fuel Slot (Soul Fragment)
        this.addSlot(new SlotItemHandler(inventory, 1, 34, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.SOUL_FRAGMENT.get());
            }
        });

        // Output Slots (Read-only)
        this.addSlot(new SlotItemHandler(inventory, 2, 90, 26) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        this.addSlot(new SlotItemHandler(inventory, 3, 108, 26) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        this.addSlot(new SlotItemHandler(inventory, 4, 126, 26) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        this.addSlot(new SlotItemHandler(inventory, 5, 90, 44) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        this.addSlot(new SlotItemHandler(inventory, 6, 108, 44) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        this.addSlot(new SlotItemHandler(inventory, 7, 126, 44) { @Override public boolean mayPlace(ItemStack stack) { return false; } });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    public boolean isSifting() {
        return data.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int arrowSize = 24;
        return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // If clicking inside the Sieve (slots 0-7)
            if (index < 8) {
                // Try to move to player inventory
                if (!this.moveItemStackTo(itemstack1, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // If clicking in Player Inventory
            else {
                // Check if it's a soul fragment and put it in the fuel slot first
                if (itemstack1.is(ModItems.SOUL_FRAGMENT.get())) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        // If fuel is full, try putting it in the input slot
                        if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                // Otherwise just try the input slot
                else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
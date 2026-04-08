package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MeasuringTapeItem extends Item {

    public MeasuringTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();

        if (player != null && !player.isCrouching()) {
            nbt.putInt("FirstX", pos.getX());
            nbt.putInt("FirstY", pos.getY());
            nbt.putInt("FirstZ", pos.getZ());
            nbt.putBoolean("HasFirst", true);
        } else {
            nbt.putInt("SecondX", pos.getX());
            nbt.putInt("SecondY", pos.getY());
            nbt.putInt("SecondZ", pos.getZ());
            nbt.putBoolean("HasSecond", true);
        }

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();

        if (player.isCrouching()) {
            String mode = nbt.getString("Mode");
            if (mode.isEmpty() || mode.equals("vector_distance")) {
                nbt.putString("Mode", "manhattan_distance");
            } else {
                nbt.putString("Mode", "vector_distance");
            }
        } else {
            nbt.remove("HasFirst");
            nbt.remove("HasSecond");
        }

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();

        boolean hasFirst = nbt.getBoolean("HasFirst");
        boolean hasSecond = nbt.getBoolean("HasSecond");
        String mode = nbt.getString("Mode");
        if (mode.isEmpty()) mode = "vector_distance";

        BlockPos firstPos = null;
        BlockPos secondPos = null;

        if (hasFirst) {
            firstPos = new BlockPos(nbt.getInt("FirstX"), nbt.getInt("FirstY"), nbt.getInt("FirstZ"));
            tooltip.add(Component.translatable("tooltip.echoesofantiquity.measuring_tape.first_pos")
                    .append(Component.literal("X: " + firstPos.getX() + ", Y: " + firstPos.getY() + ", Z: " + firstPos.getZ())));
        }

        if (hasSecond) {
            secondPos = new BlockPos(nbt.getInt("SecondX"), nbt.getInt("SecondY"), nbt.getInt("SecondZ"));
            tooltip.add(Component.translatable("tooltip.echoesofantiquity.measuring_tape.second_pos")
                    .append(Component.literal("X: " + secondPos.getX() + ", Y: " + secondPos.getY() + ", Z: " + secondPos.getZ())));
        }

        if (hasFirst && hasSecond) {
            Vec3 newVec = new Vec3(firstPos.getX() - secondPos.getX(), firstPos.getY() - secondPos.getY(), firstPos.getZ() - secondPos.getZ());
            if (mode.equals("vector_distance")) {
                double distance = newVec.length();
                String rounded = String.format("%.2f", distance + 1);
                tooltip.add(Component.translatable("tooltip.echoesofantiquity.measuring_tape.distance").append(rounded));
            } else if (mode.equals("manhattan_distance")) {
                int distance = firstPos.distManhattan(secondPos) + 1;
                tooltip.add(Component.translatable("tooltip.echoesofantiquity.measuring_tape.manhattan_distance").append(String.valueOf(distance)));
            }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}

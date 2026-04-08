package net.frostytrix.echoesofantiquity.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class VoidPedestalBERenderer implements BlockEntityRenderer<VoidPedestalBlockEntity> {
    public VoidPedestalBERenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(VoidPedestalBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getInventory().getStackInSlot(0);

        if (!stack.isEmpty()) {
            poseStack.pushPose();

            // 1. Move to the center of the block and slightly above the top
            poseStack.translate(0.5, 1.2, 0.5);

            // 2. Scale the item down
            poseStack.scale(0.5f, 0.5f, 0.5f);

            // 3. Apply the rotation from the block entity logic
            float rotation = entity.getRenderingRotation(entity.getBlockPos(), entity.getLevel());
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            // 4. Render the item
            // We use the combinedLight passed into the method for better accuracy
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLight,
                    combinedOverlay, poseStack, bufferSource, entity.getLevel(), 1);

            poseStack.popPose();
        }
    }
}
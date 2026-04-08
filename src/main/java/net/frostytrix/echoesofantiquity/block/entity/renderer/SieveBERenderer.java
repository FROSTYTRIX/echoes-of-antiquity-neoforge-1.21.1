package net.frostytrix.echoesofantiquity.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.frostytrix.echoesofantiquity.block.entity.custom.SieveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SieveBERenderer implements BlockEntityRenderer<SieveBlockEntity> {

    public SieveBERenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SieveBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ItemStack inputStack = entity.getInputStack();

        if (inputStack.isEmpty() || !entity.hasValidRecipeForRender()) {
            return;
        }

        int progress = entity.getProgress();
        int maxProgress = entity.getMaxProgress();

        poseStack.pushPose();

        float startY = 0.8f;
        float endY = 0.3f;
        float currentY = startY;

        if (progress > 0 && maxProgress > 0) {
            float smoothedProgress = progress + partialTick;
            float progressRatio = Math.min(smoothedProgress / maxProgress, 1.0f);
            currentY = startY - ((startY - endY) * progressRatio);
        }

        poseStack.translate(0.5f, currentY, 0.5f);
        poseStack.scale(1.5f, 1.5f, 1.5f);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(
                inputStack,
                ItemDisplayContext.FIXED,
                combinedLight,
                combinedOverlay,
                poseStack,
                bufferSource,
                entity.getLevel(),
                (int) entity.getBlockPos().asLong()
        );

        poseStack.popPose();
    }
}
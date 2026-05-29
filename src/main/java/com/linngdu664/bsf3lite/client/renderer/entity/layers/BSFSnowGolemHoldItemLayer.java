package com.linngdu664.bsf3lite.client.renderer.entity.layers;

import com.linngdu664.bsf3lite.client.model.BSFSnowGolemModel;
import com.linngdu664.bsf3lite.client.renderer.entity.state.BSFSnowGolemRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class BSFSnowGolemHoldItemLayer extends RenderLayer<BSFSnowGolemRenderState, BSFSnowGolemModel> {
    public BSFSnowGolemHoldItemLayer(RenderLayerParent<BSFSnowGolemRenderState, BSFSnowGolemModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, BSFSnowGolemRenderState state, float yRot, float xRot) {
        ItemStack itemstack = state.weapon;
        ItemInHandRenderer itemInHandRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(yRot * Mth.DEG_TO_RAD * 0.25F, 0F, 1F, 0F)));
            poseStack.translate(-0.05, 0.2, -0.8);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.max(state.weaponAngle - 60 * state.partialTick, 0) * Mth.DEG_TO_RAD, 1F, 0F, 0F)));
            itemInHandRenderer.renderItem(state.golem, itemstack, ItemDisplayContext.HEAD, poseStack, submitNodeCollector, i);
            poseStack.popPose();
        }
        itemstack = state.ammo;
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(yRot * Mth.DEG_TO_RAD * 0.25F, 0F, 1F, 0F)));
            itemInHandRenderer.renderItem(state.golem, itemstack, ItemDisplayContext.HEAD, poseStack, submitNodeCollector, i);
            poseStack.popPose();
        }
    }
}

package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.BSFSnowGolemModel;
import com.linngdu664.bsf3lite.client.model.IceSkatesModel;
import com.linngdu664.bsf3lite.client.model.SnowFallBootsModel;
import com.linngdu664.bsf3lite.client.renderer.entity.BSFDummyRenderer;
import com.linngdu664.bsf3lite.client.renderer.entity.BSFSnowGolemRenderer;
import com.linngdu664.bsf3lite.client.renderer.entity.SculkSnowballRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class RendererRegistry {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(IceSkatesModel.LEFT_LAYER, IceSkatesModel::createBodyLayerLeft);
        event.registerLayerDefinition(IceSkatesModel.RIGHT_LAYER, IceSkatesModel::createBodyLayerRight);
        event.registerLayerDefinition(SnowFallBootsModel.LEFT_LAYER, SnowFallBootsModel::createBodyLayerLeft);
        event.registerLayerDefinition(SnowFallBootsModel.RIGHT_LAYER, SnowFallBootsModel::createBodyLayerRight);
        event.registerLayerDefinition(BSFSnowGolemModel.LAYER_LOCATION, BSFSnowGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.BSF_SNOW_GOLEM.get(), BSFSnowGolemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HOSTILE_SNOW_GOLEM.get(), BSFSnowGolemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BSF_DUMMY.get(), BSFDummyRenderer::new);

        event.registerEntityRenderer(EntityRegistry.POWDER_EXECUTOR.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.COMPACTED_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EXPLOSIVE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.IRON_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.OBSIDIAN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.POWDER_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SMOOTH_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPECTRAL_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.STONE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EXPANSION_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.RECONSTRUCT_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ICICLE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CRITICAL_FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CHERRY_BLOSSOM_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SCULK_SNOWBALL.get(), SculkSnowballRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DUCK_SNOWBALL.get(), ThrownItemRenderer::new);
    }
}

package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.IceSkatesModel;
import com.linngdu664.bsf3lite.client.model.SnowFallBootsModel;
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
        event.registerLayerDefinition(IceSkatesModel.LAYER_LOCATION, IceSkatesModel::createBodyLayer);
        event.registerLayerDefinition(SnowFallBootsModel.LAYER_LOCATION, SnowFallBootsModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
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
        event.registerEntityRenderer(EntityRegistry.SCULK_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DUCK_SNOWBALL.get(), ThrownItemRenderer::new);
    }
}

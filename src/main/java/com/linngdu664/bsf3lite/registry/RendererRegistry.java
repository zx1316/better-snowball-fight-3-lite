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
        event.registerEntityRenderer(EntityRegister.POWDER_EXECUTOR.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegister.COMPACTED_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.EXPLOSIVE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.IRON_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.OBSIDIAN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.POWDER_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SMOOTH_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SPECTRAL_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.STONE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.EXPANSION_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.RECONSTRUCT_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.ICICLE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.CRITICAL_FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.CHERRY_BLOSSOM_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SCULK_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.DUCK_SNOWBALL.get(), ThrownItemRenderer::new);
    }
}

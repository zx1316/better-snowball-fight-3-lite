package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.executor.*;
import com.linngdu664.bsf3lite.entity.snowball.nomal.*;
import com.linngdu664.bsf3lite.entity.snowball.special.*;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Main.MODID);

   public static final DeferredHolder<EntityType<?>, EntityType<PowderExecutor>> POWDER_EXECUTOR = executorRegister(PowderExecutor::new, "powder_executor", 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<SmoothSnowballEntity>> SMOOTH_SNOWBALL = snowballRegister(SmoothSnowballEntity::new, "smooth_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<CompactedSnowballEntity>> COMPACTED_SNOWBALL = snowballRegister(CompactedSnowballEntity::new, "compacted_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<StoneSnowballEntity>> STONE_SNOWBALL = snowballRegister(StoneSnowballEntity::new, "stone_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<IceSnowballEntity>> ICE_SNOWBALL = snowballRegister(IceSnowballEntity::new, "ice_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<IronSnowballEntity>> IRON_SNOWBALL = snowballRegister(IronSnowballEntity::new, "iron_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<ObsidianSnowballEntity>> OBSIDIAN_SNOWBALL = snowballRegister(ObsidianSnowballEntity::new, "obsidian_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<ExplosiveSnowballEntity>> EXPLOSIVE_SNOWBALL = snowballRegister(ExplosiveSnowballEntity::new, "explosive_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralSnowballEntity>> SPECTRAL_SNOWBALL = snowballRegister(SpectralSnowballEntity::new, "spectral_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<FrozenSnowballEntity>> FROZEN_SNOWBALL = snowballRegister(FrozenSnowballEntity::new, "frozen_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<PowderSnowballEntity>> POWDER_SNOWBALL = snowballRegister(PowderSnowballEntity::new, "powder_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<ExpansionSnowballEntity>> EXPANSION_SNOWBALL = snowballRegister(ExpansionSnowballEntity::new, "expansion_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<ReconstructSnowballEntity>> RECONSTRUCT_SNOWBALL = snowballRegister(ReconstructSnowballEntity::new, "reconstruct_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<IcicleSnowballEntity>> ICICLE_SNOWBALL = snowballRegister(IcicleSnowballEntity::new, "icicle_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<CriticalFrozenSnowballEntity>> CRITICAL_FROZEN_SNOWBALL = snowballRegister(CriticalFrozenSnowballEntity::new, "critical_frozen_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<CherryBlossomSnowballEntity>> CHERRY_BLOSSOM_SNOWBALL = snowballRegister(CherryBlossomSnowballEntity::new, "cherry_blossom_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<SculkSnowballEntity>> SCULK_SNOWBALL = snowballRegister(SculkSnowballEntity::new, "sculk_snowball");
    public static final DeferredHolder<EntityType<?>, EntityType<DuckSnowballEntity>> DUCK_SNOWBALL = snowballRegister(DuckSnowballEntity::new, "duck_snowball");

    //A tool to register snowball entity
    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> snowballRegister(EntityType.EntityFactory<T> pFactory, String name) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(pFactory, MobCategory.MISC)
                .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                .build(ResourceKey.create(ENTITY_TYPES.getRegistryKey(), Main.makeMyIdentifier(name))));
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> executorRegister(EntityType.EntityFactory<T> pFactory, String name, float size) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(pFactory, MobCategory.MISC)
                .sized(size, size).updateInterval(10).fireImmune()
                .build(ResourceKey.create(ENTITY_TYPES.getRegistryKey(), Main.makeMyIdentifier(name))));
    }

    @EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
    public static class RendererRegister {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(POWDER_EXECUTOR.get(), NoopRenderer::new);
            event.registerEntityRenderer(COMPACTED_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(EXPLOSIVE_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(ICE_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(IRON_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(OBSIDIAN_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(POWDER_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(SMOOTH_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(SPECTRAL_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(STONE_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(EXPANSION_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(RECONSTRUCT_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(ICICLE_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(CRITICAL_FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(CHERRY_BLOSSOM_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(SCULK_SNOWBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(DUCK_SNOWBALL.get(), ThrownItemRenderer::new);
        }
    }
}


package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.BSFDummyEntity;
import com.linngdu664.bsf3lite.entity.executor.*;
import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.entity.golem.HostileSnowGolemEntity;
import com.linngdu664.bsf3lite.entity.snowball.nomal.*;
import com.linngdu664.bsf3lite.entity.snowball.special.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Main.MODID);


    public static final DeferredHolder<EntityType<?>, EntityType<BSFSnowGolemEntity>> BSF_SNOW_GOLEM =
            ENTITY_TYPES.register("bsf_snow_golem", () -> EntityType.Builder.of(BSFSnowGolemEntity::new, MobCategory.MISC)
                    .sized(0.7F, 1.9F).clientTrackingRange(8).immuneTo(Blocks.POWDER_SNOW)
                    .build(ResourceKey.create(ENTITY_TYPES.getRegistryKey(), Main.makeMyIdentifier("bsf_snow_golem"))));
    public static final DeferredHolder<EntityType<?>, EntityType<HostileSnowGolemEntity>> HOSTILE_SNOW_GOLEM =
            ENTITY_TYPES.register("region_controller_snow_golem", () -> EntityType.Builder.of(HostileSnowGolemEntity::new, MobCategory.MISC)
                    .sized(0.7F, 1.9F).clientTrackingRange(8).immuneTo(Blocks.POWDER_SNOW)
                    .build(ResourceKey.create(ENTITY_TYPES.getRegistryKey(), Main.makeMyIdentifier("hostile_snow_golem"))));
    public static final DeferredHolder<EntityType<?>, EntityType<BSFDummyEntity>> BSF_DUMMY =
            ENTITY_TYPES.register("bsf_dummy", () -> EntityType.Builder.of(BSFDummyEntity::new, MobCategory.MISC)
                    .sized(0.7F, 1.9F).clientTrackingRange(8)
                    .build(ResourceKey.create(ENTITY_TYPES.getRegistryKey(), Main.makeMyIdentifier("bsf_dummy"))));


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

    // A tool to register snowball entity
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

    @EventBusSubscriber(modid = Main.MODID)
    public static class attributeRegister {
        @SubscribeEvent
        public static void onCreateEntityAttribute(EntityAttributeCreationEvent event) {
            event.put(BSF_SNOW_GOLEM.get(), TamableAnimal.createLivingAttributes()
                    .add(Attributes.MAX_HEALTH, 15.0)
                    .add(Attributes.FOLLOW_RANGE, 60.0)
                    .add(Attributes.MOVEMENT_SPEED, 0.3)
                    .build());
            event.put(HOSTILE_SNOW_GOLEM.get(), TamableAnimal.createLivingAttributes()
                    .add(Attributes.MAX_HEALTH, 15.0)
                    .add(Attributes.FOLLOW_RANGE, 100.0)
                    .add(Attributes.MOVEMENT_SPEED, 0.3)
                    .build());
            event.put(BSF_DUMMY.get(), LivingEntity.createLivingAttributes()
                    .add(Attributes.MAX_HEALTH, Float.MAX_VALUE)
                    .add(Attributes.FOLLOW_RANGE, 60.0)
                    .add(Attributes.MOVEMENT_SPEED, 0)
                    .add(Attributes.KNOCKBACK_RESISTANCE, Double.MAX_VALUE)
                    .build());
        }
    }
}


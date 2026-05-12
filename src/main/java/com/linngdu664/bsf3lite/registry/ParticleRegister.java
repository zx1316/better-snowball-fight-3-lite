package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class ParticleRegister {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, Main.MODID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHORT_TIME_SNOWFLAKE = PARTICLES.register("short_time_snowflake", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_LONG_TIME_SNOWFLAKE = PARTICLES.register("big_long_time_snowflake", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GENERATOR_FIX = PARTICLES.register("generator_fix", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GENERATOR_PUSH = PARTICLES.register("generator_push", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> IMPLOSION_SNOWBALL_CANNON = PARTICLES.register("implosion_snowball_cannon", () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), ShortTimeSnowflake.Provider::new);
        event.registerSpriteSet(ParticleRegister.BIG_LONG_TIME_SNOWFLAKE.get(), BigLongTimeSnowflake.Provider::new);
        event.registerSpriteSet(ParticleRegister.GENERATOR_FIX.get(), GeneratorFix.Provider::new);
        event.registerSpriteSet(ParticleRegister.GENERATOR_PUSH.get(), GeneratorPush.Provider::new);
        event.registerSpriteSet(ParticleRegister.IMPLOSION_SNOWBALL_CANNON.get(), ImplosionSnowballCannonParticle.Provider::new);
    }
}

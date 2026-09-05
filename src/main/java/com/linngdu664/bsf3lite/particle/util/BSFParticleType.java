package com.linngdu664.bsf3lite.particle.util;

import com.linngdu664.bsf3lite.registry.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public enum BSFParticleType {
    SNOWFLAKE(ParticleTypes.SNOWFLAKE),
    SNOW_GOLEM_EQUIP(ParticleRegistry.SNOW_GOLEM_EQUIP_PARTICLE.get()),
    HIGH_DRAG_SNOWFLAKE(ParticleRegistry.HIGH_DRAG_SNOWFLAKE.get());

    private final ParticleOptions particleOptions;

    BSFParticleType(ParticleOptions particleOptions) {
        this.particleOptions = particleOptions;
    }

    public ParticleOptions get() {
        return particleOptions;
    }
}

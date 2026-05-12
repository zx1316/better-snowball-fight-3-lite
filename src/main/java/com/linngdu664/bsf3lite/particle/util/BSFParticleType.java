package com.linngdu664.bsf3lite.particle.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public enum BSFParticleType {
    SNOWFLAKE(ParticleTypes.SNOWFLAKE);
    private final ParticleOptions particleOptions;

    BSFParticleType(ParticleOptions particleOptions) {
        this.particleOptions = particleOptions;
    }

    public ParticleOptions get() {
        return particleOptions;
    }
}

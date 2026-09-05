package com.linngdu664.bsf3lite.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class HighDragSnowflake extends SingleQuadParticle {
    private static final float VELOCITY_RETENTION = 0.7F;
    private final SpriteSet sprites;

    protected HighDragSnowflake(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.sprites = sprites;
        this.xd = xa + (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F;
        this.yd = ya + (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F;
        this.zd = za + (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() + 1.0F);
        this.lifetime = (int) (16.0 / (this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteFromAge(sprites);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.xd *= VELOCITY_RETENTION;
        this.yd *= VELOCITY_RETENTION;
        this.zd *= VELOCITY_RETENTION;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, @NonNull ClientLevel level, double x, double y, double z,
                                                 double xa, double ya, double za, RandomSource random) {
            HighDragSnowflake particle = new HighDragSnowflake(level, x, y, z, xa, ya, za, this.sprites);
            particle.setColor(0.923F, 0.964F, 0.999F);
            return particle;
        }
    }
}

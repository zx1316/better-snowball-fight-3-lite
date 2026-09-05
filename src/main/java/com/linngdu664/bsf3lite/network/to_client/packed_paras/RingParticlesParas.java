package com.linngdu664.bsf3lite.network.to_client.packed_paras;

import com.linngdu664.bsf3lite.network.CustomStreamCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record RingParticlesParas(Vec3 center, Vec3 normal, float radius, int particleCount, float speed) {
    public static final StreamCodec<ByteBuf, RingParticlesParas> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull ByteBuf byteBuf, @NotNull RingParticlesParas paras) {
            CustomStreamCodecs.VEC3_STREAM_CODEC.encode(byteBuf, paras.center());
            CustomStreamCodecs.VEC3_STREAM_CODEC.encode(byteBuf, paras.normal());
            byteBuf.writeFloat(paras.radius());
            byteBuf.writeInt(paras.particleCount());
            byteBuf.writeFloat(paras.speed());
        }

        @Override
        public @NotNull RingParticlesParas decode(@NotNull ByteBuf byteBuf) {
            Vec3 center = CustomStreamCodecs.VEC3_STREAM_CODEC.decode(byteBuf);
            Vec3 normal = CustomStreamCodecs.VEC3_STREAM_CODEC.decode(byteBuf);
            float radius = byteBuf.readFloat();
            int particleCount = byteBuf.readInt();
            float speed = byteBuf.readFloat();
            return new RingParticlesParas(center, normal, radius, particleCount, speed);
        }
    };
}

package com.linngdu664.bsf3lite.network.to_client.packed_paras;

import com.linngdu664.bsf3lite.network.CustomStreamCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * @param pos1            Cuboid vertex 1
 * @param pos2            Cuboid vertex 2
 * @param velDirection    Velocity vector
 * @param vMin            Speed minimum
 * @param vMax            Speed maximum
 * @param num             Number of particles
 */
public record ForwardRaysParticlesParas(Vec3 pos1, Vec3 pos2, Vec3 velDirection, double vMin, double vMax, int num) {
    public static final StreamCodec<ByteBuf, ForwardRaysParticlesParas> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull ByteBuf byteBuf, @NotNull ForwardRaysParticlesParas paras) {
            CustomStreamCodecs.VEC3_STREAM_CODEC.encode(byteBuf, paras.pos1());
            CustomStreamCodecs.VEC3_STREAM_CODEC.encode(byteBuf, paras.pos2());
            CustomStreamCodecs.VEC3_STREAM_CODEC.encode(byteBuf, paras.velDirection());
            byteBuf.writeDouble(paras.vMin());
            byteBuf.writeDouble(paras.vMax());
            byteBuf.writeInt(paras.num());
        }

        @Override
        public @NotNull ForwardRaysParticlesParas decode(@NotNull ByteBuf byteBuf) {
            Vec3 pos1 = CustomStreamCodecs.VEC3_STREAM_CODEC.decode(byteBuf);
            Vec3 pos2 = CustomStreamCodecs.VEC3_STREAM_CODEC.decode(byteBuf);
            Vec3 velDirection = CustomStreamCodecs.VEC3_STREAM_CODEC.decode(byteBuf);
            double vMin = byteBuf.readDouble();
            double vMax = byteBuf.readDouble();
            int num = byteBuf.readInt();
            return new ForwardRaysParticlesParas(pos1, pos2, velDirection, vMin, vMax, num);
        }
    };
}

package com.linngdu664.bsf3lite.network.to_client;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.network.to_client.packed_paras.RingParticlesParas;
import com.linngdu664.bsf3lite.particle.util.BSFParticleType;
import com.linngdu664.bsf3lite.particle.util.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RingParticlesPayload(RingParticlesParas paras, int particleType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RingParticlesPayload> TYPE = new CustomPacketPayload.Type<>(Main.makeMyIdentifier("ring_particles"));
    public static final StreamCodec<ByteBuf, RingParticlesPayload> STREAM_CODEC = StreamCodec.composite(
            RingParticlesParas.STREAM_CODEC, RingParticlesPayload::paras,
            ByteBufCodecs.VAR_INT, RingParticlesPayload::particleType,
            RingParticlesPayload::new
    );

    public static void handleDataInClient(RingParticlesPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ParticleUtil.spawnRingParticles(context.player().level(), BSFParticleType.values()[payload.particleType].get(), payload.paras));
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

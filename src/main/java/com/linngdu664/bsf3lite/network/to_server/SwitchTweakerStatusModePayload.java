package com.linngdu664.bsf3lite.network.to_server;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SwitchTweakerStatusModePayload(boolean isIncrease) implements CustomPacketPayload {
    public static final Type<SwitchTweakerStatusModePayload> TYPE = new Type<>(Main.makeMyIdentifier("switch_tweaker_status_mode"));
    public static final StreamCodec<ByteBuf, SwitchTweakerStatusModePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SwitchTweakerStatusModePayload::isIncrease,
            SwitchTweakerStatusModePayload::new
    );

    public static void handleDataInServer(SwitchTweakerStatusModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemStack.getItem().equals(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get())) {
                Level level = sender.level();
                int status = itemStack.getOrDefault(DataComponentRegistry.TWEAKER_STATUS_MODE.get(), (byte) 0);
                if (payload.isIncrease) {
                    status = status + 1 >= 5 ? 0 : status + 1;
                } else {
                    status = status <= 0 ? 4 : status - 1;
                }
                itemStack.set(DataComponentRegistry.TWEAKER_STATUS_MODE.get(), (byte) status);
                level.playSound(null, sender.getX(), sender.getY(), sender.getZ(), SoundEvents.DISPENSER_DISPENSE, SoundSource.PLAYERS, 6.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

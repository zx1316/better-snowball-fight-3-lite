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

public record SwitchTweakerTargetModePayload(boolean isIncrease) implements CustomPacketPayload {
    public static final Type<SwitchTweakerTargetModePayload> TYPE = new Type<>(Main.makeMyIdentifier("switch_tweaker_target_mode"));
    public static final StreamCodec<ByteBuf, SwitchTweakerTargetModePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SwitchTweakerTargetModePayload::isIncrease,
            SwitchTweakerTargetModePayload::new
    );

    public static void handleDataInServer(SwitchTweakerTargetModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemStack.getItem().equals(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get())) {
                Level level = sender.level();
                int targetMode = itemStack.getOrDefault(DataComponentRegistry.TWEAKER_TARGET_MODE.get(), (byte) 0);
                if (payload.isIncrease) {
                    targetMode = targetMode + 1 >= 2 ? 0 : targetMode + 1;
                } else {
                    targetMode = targetMode <= 0 ? 1 : targetMode - 1;
                }
                itemStack.set(DataComponentRegistry.TWEAKER_TARGET_MODE.get(), (byte) targetMode);
                level.playSound(null, sender.getX(), sender.getY(), sender.getZ(), SoundEvents.DISPENSER_DISPENSE, SoundSource.PLAYERS, 6.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

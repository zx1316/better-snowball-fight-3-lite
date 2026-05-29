package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.network.to_client.*;
import com.linngdu664.bsf3lite.network.to_server.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Main.MODID)
public class NetworkRegistry {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ForwardConeParticlesPayload.TYPE, ForwardConeParticlesPayload.STREAM_CODEC, ForwardConeParticlesPayload::handleDataInClient);
        registrar.playToClient(ForwardRaysParticlesPayload.TYPE, ForwardRaysParticlesPayload.STREAM_CODEC, ForwardRaysParticlesPayload::handleDataInClient);
        registrar.playToClient(ImplosionSnowballCannonParticlesPayload.TYPE, ImplosionSnowballCannonParticlesPayload.STREAM_CODEC, ImplosionSnowballCannonParticlesPayload::handleDataInClient);
        registrar.playToClient(ScreenshakePayload.TYPE, ScreenshakePayload.STREAM_CODEC, ScreenshakePayload::handleDataInClient);
        registrar.playToClient(ToggleMovingSoundPayload.TYPE, ToggleMovingSoundPayload.STREAM_CODEC, ToggleMovingSoundPayload::handleDataInClient);

        registrar.playToServer(AmmoTypePayload.TYPE, AmmoTypePayload.STREAM_CODEC, AmmoTypePayload::handleDataInServer);
        registrar.playToServer(SculkSnowballLauncherSwitchSoundPayload.TYPE, SculkSnowballLauncherSwitchSoundPayload.STREAM_CODEC, SculkSnowballLauncherSwitchSoundPayload::handleDataInServer);
        registrar.playToServer(SwitchTweakerStatusModePayload.TYPE, SwitchTweakerStatusModePayload.STREAM_CODEC, SwitchTweakerStatusModePayload::handleDataInServer);
        registrar.playToServer(SwitchTweakerTargetModePayload.TYPE, SwitchTweakerTargetModePayload.STREAM_CODEC, SwitchTweakerTargetModePayload::handleDataInServer);
    }
}

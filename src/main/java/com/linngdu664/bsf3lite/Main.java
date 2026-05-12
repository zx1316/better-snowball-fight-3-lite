package com.linngdu664.bsf3lite;

import com.linngdu664.bsf3lite.config.ClientConfig;
import com.linngdu664.bsf3lite.config.ServerConfig;
import com.linngdu664.bsf3lite.registry.*;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "bsf3lite";

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        BlockRegister.BLOCKS.register(modEventBus);
        BlockEntityRegister.BLOCK_ENTITIES.register(modEventBus);
        DataComponentRegister.DATA_COMPONENTS.register(modEventBus);
        ItemRegister.ITEMS.register(modEventBus);
        SoundRegister.SOUNDS.register(modEventBus);
        ParticleRegister.PARTICLES.register(modEventBus);
        EffectRegister.EFFECTS.register(modEventBus);
        EntityRegister.ENTITY_TYPES.register(modEventBus);
        CreativeTabRegister.CREATIVE_TABS.register(modEventBus);
        TriggerTypeRegister.TRIGGER_TYPES.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    public static Identifier makeMyIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}

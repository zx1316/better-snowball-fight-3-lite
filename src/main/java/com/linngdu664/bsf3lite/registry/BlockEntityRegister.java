package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Main.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CriticalSnowEntity>> CRITICAL_SNOW = BLOCK_ENTITIES.register("critical_snow", () -> new CriticalSnowEntity(), BlockRegister.CRITICAL_SNOW.get());
}

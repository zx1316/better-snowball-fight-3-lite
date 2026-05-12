package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegister {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Main.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemData>> AMMO_ITEM =
            DATA_COMPONENTS.registerComponentType(
                    "ammo_item",
                    builder -> builder.persistent(ItemData.CODEC).networkSynchronized(ItemData.STREAM_CODEC)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SCULK_SOUND_ID =
            DATA_COMPONENTS.registerComponentType(
                    "sculk_sound_id",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> BASIN_SNOW_TYPE =
            DATA_COMPONENTS.registerComponentType(
                    "basin_snow_type",
                    builder -> builder.persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MACHINE_GUN_TIMER =
            DATA_COMPONENTS.registerComponentType(
                    "machine_gun_timer",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> MACHINE_GUN_IS_COOL_DOWN =
            DATA_COMPONENTS.registerComponentType(
                    "machine_gun_is_cool_down",
                    builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RegionData>> REGION =
            DATA_COMPONENTS.registerComponentType(
                    "region",
                    builder -> builder.persistent(RegionData.CODEC).networkSynchronized(RegionData.STREAM_CODEC)
            );
}

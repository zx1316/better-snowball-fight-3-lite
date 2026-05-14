package com.linngdu664.bsf3lite.client.renderer.item.properties.numeric;

import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Snowball implements RangeSelectItemModelProperty {
    public static final MapCodec<Snowball> MAP_CODEC = MapCodec.unit(new Snowball());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable ItemOwner itemOwner, int i) {
        Item item = itemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
        if (item instanceof AbstractBSFSnowballItem snowballItem) {
            return snowballItem.getIdForTank();
        }
        return -1;
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}

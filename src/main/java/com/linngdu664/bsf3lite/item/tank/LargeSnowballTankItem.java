package com.linngdu664.bsf3lite.item.tank;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class LargeSnowballTankItem extends SnowballTankItem {
    public LargeSnowballTankItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("large_snowball_tank")))
                .stacksTo(1)
                .durability(192));
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        Item item = pStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
        if (!Items.AIR.equals(item)) {
            String path = BuiltInRegistries.ITEM.getKey(item).getPath();
            return MutableComponent.create(new TranslatableContents("item.bsf3lite.large_" + path + "_tank", null, new Object[0]));
        }
        return super.getName(pStack);
    }
}

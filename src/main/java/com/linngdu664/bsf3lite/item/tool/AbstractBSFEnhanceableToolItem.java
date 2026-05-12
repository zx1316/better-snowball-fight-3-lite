package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public abstract class AbstractBSFEnhanceableToolItem extends Item {
    public AbstractBSFEnhanceableToolItem(String id, Rarity rarity, int durability) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .stacksTo(1)
                .rarity(rarity)
                .durability(durability)
                .enchantable(1));
    }

    public AbstractBSFEnhanceableToolItem(String id, Rarity rarity, int durability, Item repairItem) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .stacksTo(1)
                .rarity(rarity)
                .durability(durability)
                .enchantable(1)
                .repairable(repairItem));
    }

    public AbstractBSFEnhanceableToolItem(String id, Rarity rarity, int durability, TagKey<Item> repairItemTag) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .stacksTo(1)
                .rarity(rarity)
                .durability(durability)
                .enchantable(1)
                .repairable(repairItemTag));
    }
}

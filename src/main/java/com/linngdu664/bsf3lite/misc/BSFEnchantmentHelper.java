package com.linngdu664.bsf3lite.misc;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;

public class BSFEnchantmentHelper {
    public static final ResourceKey<Enchantment> FLOATING_SHOOTING = ResourceKey.create(Registries.ENCHANTMENT, Main.makeMyIdentifier("floating_shooting"));
    public static final ResourceKey<Enchantment> KINETIC_ENERGY_STORAGE = ResourceKey.create(Registries.ENCHANTMENT, Main.makeMyIdentifier("kinetic_energy_storage"));
    public static final ResourceKey<Enchantment> SNOW_GOLEM_EXCLUSIVE = ResourceKey.create(Registries.ENCHANTMENT, Main.makeMyIdentifier("snow_golem_exclusive"));

    public static Holder<Enchantment> getEnchantmentHolder(Entity entity, ResourceKey<Enchantment> resourceKey) {
        return entity.registryAccess().lookup(Registries.ENCHANTMENT).map(enchantmentRegistryLookup -> enchantmentRegistryLookup.getOrThrow(resourceKey)).orElse(null);
    }
}

package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.effect.ColdResistanceEffect;
import com.linngdu664.bsf3lite.effect.WeaponJamEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Main.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> COLD_RESISTANCE = EFFECTS.register("cold_resistance", ColdResistanceEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> WEAPON_JAM = EFFECTS.register("weapon_jam", WeaponJamEffect::new);
}

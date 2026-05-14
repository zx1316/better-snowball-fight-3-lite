package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.criterion_trigger.SnowballDamageTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TriggerTypeRegistry {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, Main.MODID);
    public static final DeferredHolder<CriterionTrigger<?>, SnowballDamageTrigger> SNOWBALL_DAMAGE_TRIGGER = TRIGGER_TYPES.register("snowball_damage_trigger", SnowballDamageTrigger::new);
}

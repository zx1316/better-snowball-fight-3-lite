package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.ItemData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

    // Creates a creative tab with the id "bsf:bsf_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BSF_TAB = CREATIVE_TABS.register("bsf_tab", () -> net.minecraft.world.item.CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ItemRegistry.EXPLOSIVE_SNOWBALL.get().getDefaultInstance())
            .title(MutableComponent.create(new TranslatableContents("itemGroup.bsf_group", null, new Object[0])))
            .displayItems((parameters, output) -> {
                output.accept(ItemRegistry.SMOOTH_SNOWBALL.get());
                output.accept(ItemRegistry.DUCK_SNOWBALL.get());
                output.accept(ItemRegistry.COMPACTED_SNOWBALL.get());
                output.accept(ItemRegistry.COMPACTED_SNOWBALL_SET.get());
                output.accept(ItemRegistry.CHERRY_BLOSSOM_SNOWBALL.get());
                output.accept(ItemRegistry.STONE_SNOWBALL.get());
                output.accept(ItemRegistry.ICE_SNOWBALL.get());
                output.accept(ItemRegistry.IRON_SNOWBALL.get());
                output.accept(ItemRegistry.OBSIDIAN_SNOWBALL.get());
                output.accept(ItemRegistry.EXPLOSIVE_SNOWBALL.get());

                output.accept(ItemRegistry.POWDER_SNOWBALL.get());
                output.accept(ItemRegistry.SPECTRAL_SNOWBALL.get());
                output.accept(ItemRegistry.FROZEN_SNOWBALL.get());
                output.accept(ItemRegistry.CRITICAL_FROZEN_SNOWBALL.get());

                output.accept(ItemRegistry.EXPANSION_SNOWBALL.get());
                output.accept(ItemRegistry.RECONSTRUCT_SNOWBALL.get());
                output.accept(ItemRegistry.ICICLE_SNOWBALL.get());



                Item tank = ItemRegistry.SNOWBALL_TANK.get();
                ItemStack itemStack = tank.getDefaultInstance();
                itemStack.setDamageValue(itemStack.getMaxDamage());
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.COMPACTED_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.CHERRY_BLOSSOM_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.STONE_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.ICE_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.IRON_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.OBSIDIAN_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.EXPLOSIVE_SNOWBALL.get()));
                output.accept(itemStack);

                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.POWDER_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.SPECTRAL_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.FROZEN_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.CRITICAL_FROZEN_SNOWBALL.get()));
                output.accept(itemStack);

                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.EXPANSION_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.RECONSTRUCT_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.ICICLE_SNOWBALL.get()));
                output.accept(itemStack);


                tank = ItemRegistry.LARGE_SNOWBALL_TANK.get();
                itemStack = tank.getDefaultInstance();
                itemStack.setDamageValue(itemStack.getMaxDamage());
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.COMPACTED_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.CHERRY_BLOSSOM_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.STONE_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.ICE_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.IRON_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.OBSIDIAN_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.EXPLOSIVE_SNOWBALL.get()));
                output.accept(itemStack);

                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.POWDER_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.SPECTRAL_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.FROZEN_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.CRITICAL_FROZEN_SNOWBALL.get()));
                output.accept(itemStack);

                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.EXPANSION_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.RECONSTRUCT_SNOWBALL.get()));
                output.accept(itemStack);
                itemStack = tank.getDefaultInstance();
                itemStack.set(DataComponentRegistry.AMMO_ITEM, new ItemData(ItemRegistry.ICICLE_SNOWBALL.get()));
                output.accept(itemStack);

                output.accept(ItemRegistry.WOOD_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.STONE_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.IRON_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.GOLD_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.DIAMOND_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.NETHERITE_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.EMERALD_SNOWBALL_CLAMP.get());
                output.accept(ItemRegistry.BASIN.get());
                itemStack = ItemRegistry.BASIN.get().getDefaultInstance();
                itemStack.set(DataComponentRegistry.BASIN_SNOW_TYPE, (byte) 1);
                output.accept(itemStack);
                itemStack = ItemRegistry.BASIN.get().getDefaultInstance();
                itemStack.set(DataComponentRegistry.BASIN_SNOW_TYPE, (byte) 2);
                output.accept(itemStack);
                output.accept(ItemRegistry.SNOWBALL_CANNON.get());
                output.accept(ItemRegistry.POWERFUL_SNOWBALL_CANNON.get());
                output.accept(ItemRegistry.FREEZING_SNOWBALL_CANNON.get());
                output.accept(ItemRegistry.IMPLOSION_SNOWBALL_CANNON.get());
                output.accept(ItemRegistry.SNOWBALL_RIFLE.get());
                output.accept(ItemRegistry.POWERFUL_SNOWBALL_RIFLE.get());
                output.accept(ItemRegistry.FREEZING_SNOWBALL_RIFLE.get());
                output.accept(ItemRegistry.SNOWBALL_MACHINE_GUN.get());
                output.accept(ItemRegistry.SNOWBALL_SHOTGUN.get());
                output.accept(ItemRegistry.SNOWMAN_IN_HAND.get());
                output.accept(ItemRegistry.SCULK_SNOWBALL_LAUNCHER.get());

                output.accept(ItemRegistry.SNOW_BLOCK_BLENDER.get());
                output.accept(ItemRegistry.SNOW_TRAP_SETTER.get());
                output.accept(ItemRegistry.REPULSIVE_FIELD_GENERATOR.get());
                output.accept(ItemRegistry.GLOVE.get());
                output.accept(ItemRegistry.JEDI_GLOVE.get());
                output.accept(ItemRegistry.COLD_COMPRESSION_JET_ENGINE.get());

                output.accept(ItemRegistry.ICE_SKATES.get());
                output.accept(ItemRegistry.SNOW_FALL_BOOTS.get());

                output.accept(ItemRegistry.POPSICLE.get());
                output.accept(ItemRegistry.MILK_POPSICLE.get());
                output.accept(ItemRegistry.VODKA.get());

                output.accept(ItemRegistry.SMART_SNOW_BLOCK.get());
                output.accept(ItemRegistry.SNOW_GOLEM_CONTAINER.get());
                output.accept(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get());
                output.accept(ItemRegistry.CREATIVE_SNOW_GOLEM_TOOL.get());

                output.accept(ItemRegistry.SNOWBALL_CANNON_UPGRADE_SMITHING_TEMPLATE.get());
                output.accept(ItemRegistry.SUPER_POWER_CORE.get());
                output.accept(ItemRegistry.SUPER_FROZEN_CORE.get());

                output.accept(ItemRegistry.REPULSION_CORE.get());
                output.accept(ItemRegistry.GRAVITY_CORE.get());
                output.accept(ItemRegistry.UNSTABLE_CORE.get());
            }).build());
}

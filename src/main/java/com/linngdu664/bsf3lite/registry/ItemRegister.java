package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.minigame_tool.RegionToolItem;
import com.linngdu664.bsf3lite.item.misc.*;
import com.linngdu664.bsf3lite.item.snowball.CompactedSnowballSetItem;
import com.linngdu664.bsf3lite.item.snowball.normal.*;
import com.linngdu664.bsf3lite.item.snowball.special.*;
import com.linngdu664.bsf3lite.item.tank.LargeSnowballTankItem;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.item.tool.*;
import com.linngdu664.bsf3lite.item.weapon.*;
import com.linngdu664.bsf3lite.misc.BSFToolMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class ItemRegister {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);

    public static final DeferredItem<Item> SMOOTH_SNOWBALL = ITEMS.register("smooth_snowball", SmoothSnowballItem::new);
    public static final DeferredItem<Item> DUCK_SNOWBALL = ITEMS.register("duck_snowball", DuckSnowballItem::new);
    public static final DeferredItem<Item> COMPACTED_SNOWBALL = ITEMS.register("compacted_snowball", CompactedSnowballItem::new);
    public static final DeferredItem<Item> COMPACTED_SNOWBALL_SET = ITEMS.register("compacted_snowball_set", CompactedSnowballSetItem::new);
    public static final DeferredItem<Item> STONE_SNOWBALL = ITEMS.register("stone_snowball", StoneSnowballItem::new);
    public static final DeferredItem<Item> ICE_SNOWBALL = ITEMS.register("ice_snowball", IceSnowballItem::new);
    public static final DeferredItem<Item> IRON_SNOWBALL = ITEMS.register("iron_snowball", IronSnowballItem::new);
    public static final DeferredItem<Item> OBSIDIAN_SNOWBALL = ITEMS.register("obsidian_snowball", ObsidianSnowballItem::new);
    public static final DeferredItem<Item> EXPLOSIVE_SNOWBALL = ITEMS.register("explosive_snowball", ExplosiveSnowballItem::new);
    public static final DeferredItem<Item> SPECTRAL_SNOWBALL = ITEMS.register("spectral_snowball", SpectralSnowballItem::new);
    public static final DeferredItem<Item> FROZEN_SNOWBALL = ITEMS.register("frozen_snowball", FrozenSnowballItem::new);
    public static final DeferredItem<Item> POWDER_SNOWBALL = ITEMS.register("powder_snowball", PowderSnowballItem::new);
    public static final DeferredItem<Item> EXPANSION_SNOWBALL = ITEMS.register("expansion_snowball", ExpansionSnowballItem::new);
    public static final DeferredItem<Item> RECONSTRUCT_SNOWBALL = ITEMS.register("reconstruct_snowball", ReconstructSnowballItem::new);
    public static final DeferredItem<Item> ICICLE_SNOWBALL = ITEMS.register("icicle_snowball", IcicleSnowballItem::new);
    public static final DeferredItem<Item> CRITICAL_FROZEN_SNOWBALL = ITEMS.register("critical_frozen_snowball", CriticalFrozenSnowballItem::new);
    public static final DeferredItem<Item> CHERRY_BLOSSOM_SNOWBALL = ITEMS.register("cherry_blossom_snowball", CherryBlossomSnowballItem::new);

    public static final DeferredItem<Item> SNOWBALL_TANK = ITEMS.register("snowball_tank", SnowballTankItem::new);
    public static final DeferredItem<Item> LARGE_SNOWBALL_TANK = ITEMS.register("large_snowball_tank", LargeSnowballTankItem::new);

    public static final DeferredItem<Item> WOOD_SNOWBALL_CLAMP = ITEMS.register("wood_snowball_clamp", () -> new SnowballClampItem("wood_snowball_clamp", ToolMaterial.WOOD, 118));
    public static final DeferredItem<Item> STONE_SNOWBALL_CLAMP = ITEMS.register("stone_snowball_clamp", () -> new SnowballClampItem("stone_snowball_clamp", ToolMaterial.STONE, 260));
    public static final DeferredItem<Item> IRON_SNOWBALL_CLAMP = ITEMS.register("iron_snowball_clamp", () -> new SnowballClampItem("iron_snowball_clamp", ToolMaterial.IRON, 500));
    public static final DeferredItem<Item> GOLD_SNOWBALL_CLAMP = ITEMS.register("gold_snowball_clamp", () -> new SnowballClampItem("gold_snowball_clamp", ToolMaterial.GOLD, 64));
    public static final DeferredItem<Item> DIAMOND_SNOWBALL_CLAMP = ITEMS.register("diamond_snowball_clamp", () -> new SnowballClampItem("diamond_snowball_clamp", ToolMaterial.DIAMOND, 3122));
    public static final DeferredItem<Item> NETHERITE_SNOWBALL_CLAMP = ITEMS.register("netherite_snowball_clamp", () -> new SnowballClampItem("netherite_snowball_clamp", ToolMaterial.NETHERITE, 4062));
    public static final DeferredItem<Item> EMERALD_SNOWBALL_CLAMP = ITEMS.register("emerald_snowball_clamp", () -> new SnowballClampItem("emerald_snowball_clamp", BSFToolMaterials.EMERALD, 2866));
    public static final DeferredItem<Item> SNOWBALL_CANNON = ITEMS.register("snowball_cannon", SnowballCannonItem::new);
    public static final DeferredItem<Item> POWERFUL_SNOWBALL_CANNON = ITEMS.register("powerful_snowball_cannon", PowerfulSnowballCannonItem::new);
    public static final DeferredItem<Item> FREEZING_SNOWBALL_CANNON = ITEMS.register("freezing_snowball_cannon", FreezingSnowballCannonItem::new);
    public static final DeferredItem<Item> IMPLOSION_SNOWBALL_CANNON = ITEMS.register("implosion_snowball_cannon", ImplosionSnowballCannonItem::new);
    public static final DeferredItem<Item> SNOWBALL_MACHINE_GUN = ITEMS.register("snowball_machine_gun", SnowballMachineGunItem::new);
    public static final DeferredItem<Item> SNOWBALL_SHOTGUN = ITEMS.register("snowball_shotgun", SnowballShotgunItem::new);
    public static final DeferredItem<Item> GLOVE = ITEMS.register("glove", GloveItem::new);
    public static final DeferredItem<Item> JEDI_GLOVE = ITEMS.register("jedi_glove", JediGloveItem::new);
    public static final DeferredItem<Item> REPULSIVE_FIELD_GENERATOR = ITEMS.register("repulsive_field_generator", RepulsiveFieldGeneratorItem::new);


    public static final DeferredItem<Item> ICE_SKATES_ITEM = ITEMS.register("ice_skates", IceSkatesItem::new);
    public static final DeferredItem<Item> SNOW_FALL_BOOTS = ITEMS.register("snow_fall_boots", SnowFallBootsItem::new);
    public static final DeferredItem<Item> SNOW_BLOCK_BLENDER = ITEMS.register("snow_block_blender", SnowBlockBlenderItem::new);
    public static final DeferredItem<Item> BASIN = ITEMS.register("basin", BasinItem::new);
    public static final DeferredItem<Item> SNOW_TRAP_SETTER = ITEMS.register("snow_trap_setter", SnowTrapSetterItem::new);
    public static final DeferredItem<Item> SCULK_SNOWBALL_LAUNCHER = ITEMS.register("sculk_snowball_launcher", SculkSnowballLauncherItem::new);
    public static final DeferredItem<Item> COLD_COMPRESSION_JET_ENGINE = ITEMS.register("cold_compression_jet_engine", ColdCompressionJetEngineItem::new);


    public static final DeferredItem<Item> POPSICLE = ITEMS.register("popsicle", PopsicleItem::new);
    public static final DeferredItem<Item> MILK_POPSICLE = ITEMS.register("milk_popsicle", MilkPopsicleItem::new);
    public static final DeferredItem<Item> VODKA = ITEMS.register("vodka", VodkaItem::new);


    public static final DeferredItem<Item> SUPER_POWER_CORE = ITEMS.register("super_power_core", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> SUPER_FROZEN_CORE = ITEMS.register("super_frozen_core", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> UNSTABLE_CORE = ITEMS.register("unstable_core", UnstableCoreItem::new);
    public static final DeferredItem<Item> REPULSION_CORE = ITEMS.register("repulsion_core", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> GRAVITY_CORE = ITEMS.register("gravity_core", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> SNOWBALL_CANNON_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("snowball_cannon_upgrade_smithing_template", SnowballCannonUpgradeSmithingTemplateItem::new);
    public static final DeferredItem<Item> SNOWMAN_IN_HAND = ITEMS.register("snowman_in_hand", SnowmanInHandItem::new);


    public static final DeferredItem<Item> SCULK_SNOWBALL = ITEMS.register("sculk_snowball", () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> REGION_TOOL = ITEMS.register("region_tool", RegionToolItem::new);
}

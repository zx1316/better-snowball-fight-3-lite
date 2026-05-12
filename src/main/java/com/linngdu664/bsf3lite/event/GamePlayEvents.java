package com.linngdu664.bsf3lite.event;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.misc.SnowFallBootsItem;
import com.linngdu664.bsf3lite.item.snowball.normal.SmoothSnowballItem;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegister;
import com.linngdu664.bsf3lite.registry.EffectRegister;
import com.linngdu664.bsf3lite.registry.ItemRegister;
import com.linngdu664.bsf3lite.misc.BSFEnchantmentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Main.MODID)
public class GamePlayEvents {
    private static final AttributeModifier SKATES_SPEED_BUFF = new AttributeModifier(Main.makeMyIdentifier("skates_speed"), 0.15, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier SKATES_SPEED_DEBUFF = new AttributeModifier(Main.makeMyIdentifier("skates_speed"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @SubscribeEvent
    public static void onLivingUseItemTick(LivingEntityUseItemEvent.Tick event) {
        LivingEntity livingEntity = event.getEntity();
        ItemStack itemStack = event.getItem();
        if (itemStack.has(DataComponentRegister.REGION) && !itemStack.get(DataComponentRegister.REGION).inRegion(event.getEntity().getOnPos())) {
            event.setCanceled(true);
            return;
        }
        if (EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(livingEntity, BSFEnchantmentHelper.FLOATING_SHOOTING), itemStack) > 0) {
            double vy = livingEntity.getDeltaMovement().y;
            if (vy < 0) {
                livingEntity.resetFallDistance();
                livingEntity.push(0, -0.25 * vy, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        if (itemStack.has(DataComponentRegister.REGION.get()) && !item.equals(ItemRegister.REGION_TOOL.get()) && !itemStack.get(DataComponentRegister.REGION.get()).inRegion(event.getEntity().position())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        if (itemStack.has(DataComponentRegister.REGION.get()) && !item.equals(ItemRegister.REGION_TOOL.get()) && !itemStack.get(DataComponentRegister.REGION.get()).inRegion(event.getHitVec().getLocation())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (!itemStack.getItem().equals(ItemRegister.REGION_TOOL.get()) && itemStack.has(DataComponentRegister.REGION.get())) {
            RegionData region = event.getItemStack().get(DataComponentRegister.REGION.get());
            List<Component> list = event.getToolTip();
            list.add(Component.translatable(
                    "region_limit.tooltip",
                    String.valueOf(region.start().getX()),
                    String.valueOf(region.start().getY()),
                    String.valueOf(region.start().getZ()),
                    String.valueOf(region.end().getX()),
                    String.valueOf(region.end().getY()),
                    String.valueOf(region.end().getZ())
            ).withStyle(ChatFormatting.GRAY));
            Entity entity = event.getEntity();
            if (entity != null && !region.inRegion(entity.position())) {
                list.add(Component.translatable("region_cannot_use.tooltip").withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        Level level = player.level();
        if (!level.isClientSide() && !player.isSpectator() && entity instanceof LivingEntity target) {
            Item item = player.getMainHandItem().getItem();
            if (item instanceof SolidBucketItem) {
                if (!(target instanceof SnowGolem)) {
                    if (target.getTicksFrozen() < 240) {
                        target.setTicksFrozen(240);
                    }
                    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
                    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 2));
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 1));
                }
                target.addEffect(new MobEffectInstance(EffectRegister.WEAPON_JAM, 80, 0));
                ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, target.getX(), target.getEyeY(), target.getZ(), 16, 0, 0, 0, 0);
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getEyeY(), target.getZ(), 16, 0, 0, 0, 0.04);
                if (target instanceof Blaze) {
                    target.hurt(level.damageSources().playerAttack(player), 8);
                }
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(Items.BUCKET), true);
                }
            } else if (item instanceof SnowballItem || item instanceof SmoothSnowballItem) {
                if (!(target instanceof SnowGolem)) {
                    if (target.getTicksFrozen() < 180) {
                        target.setTicksFrozen(180);
                    }
                    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 1));
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1));
                }
                target.addEffect(new MobEffectInstance(EffectRegister.WEAPON_JAM, 40, 0));
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                }
                ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, target.getX(), target.getEyeY(), target.getZ(), 8, 0, 0, 0, 0);
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getEyeY(), target.getZ(), 8, 0, 0, 0, 0.04);
                if (target instanceof Blaze) {
                    target.hurt(level.damageSources().playerAttack(player), 4);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack itemStack = event.getCrafting();
        if (itemStack.getItem() instanceof SnowballTankItem) {
            itemStack.setDamageValue(itemStack.getMaxDamage());
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            Level level = player.level();
            ItemStack shoes = player.getItemBySlot(EquipmentSlot.FEET);
            if (!level.isClientSide() && shoes.getItem() instanceof SnowFallBootsItem) {
                int i = Mth.floor(player.getX());
                int j = Mth.floor(player.getY());
                int k = Mth.floor(player.getZ());
                Block block1 = level.getBlockState(new BlockPos(i, j, k)).getBlock();
                //Block block2 = level.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
                if (level.getBlockState(new BlockPos(i, j, k)).is(BlockTags.SNOW) || level.getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.SNOW) || snowAroundPlayer(level, player, block1)) {
                    event.setDamageMultiplier(0);
                    double h = event.getDistance();
                    ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY(), player.getZ(), (int) h * 8, 0, 0, 0, h * 0.01);
                    shoes.hurtAndBreak((int) Math.ceil((h - 3) * 0.25), player, EquipmentSlot.FEET);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    int enchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(player, BSFEnchantmentHelper.KINETIC_ENERGY_STORAGE), shoes);
                    if (enchantmentLevel > 0 && h > 5) {
                        player.addEffect(new MobEffectInstance(MobEffects.SPEED, (int) h * 6, enchantmentLevel - 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(Identifier.withDefaultNamespace("chests/shipwreck_treasure")) || event.getName().equals(Identifier.withDefaultNamespace("chests/igloo_chest"))) {
            LootTable lootTable = event.getTable();
            lootTable.addPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .setBonusRolls(ConstantValue.exactly(0.0F))
                    .add(LootItem.lootTableItem(ItemRegister.SNOWBALL_CANNON_UPGRADE_SMITHING_TEMPLATE.get()))
                    .build());
            event.setTable(lootTable);
        } else if (event.getName().equals(Identifier.withDefaultNamespace("chests/pillager_outpost"))) {
            LootTable lootTable = event.getTable();
            lootTable.addPool(LootPool.lootPool()
                    .setRolls(BinomialDistributionGenerator.binomial(2, 0.4F))
                    .setBonusRolls(ConstantValue.exactly(0.0F))
                    .add(LootItem.lootTableItem(ItemRegister.SNOWBALL_CANNON_UPGRADE_SMITHING_TEMPLATE.get()))
                    .build());
            event.setTable(lootTable);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack shoes = player.getItemBySlot(EquipmentSlot.FEET);
        AttributeMap attributes = player.getAttributes();
        if (!shoes.isEmpty() && shoes.getItem().equals(ItemRegister.ICE_SKATES.get()) && player.isSprinting() && player.onGround()) {
            Level level = player.level();
            BlockPos pos = player.blockPosition().below();
            if (level.getBlockState(pos).is(BlockTags.ICE)) {
                level.addParticle(ParticleTypes.SNOWFLAKE, player.getX(), player.getEyeY() - 1.4, player.getZ(), 0, 0, 0);
                attributes.getInstance(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(SKATES_SPEED_BUFF);
            } else {
                attributes.getInstance(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(SKATES_SPEED_DEBUFF);
            }
        } else {
            attributes.getInstance(Attributes.MOVEMENT_SPEED).removeModifier(Main.makeMyIdentifier("skates_speed"));
        }
    }

    private static boolean snowAroundPlayer(Level level, Player player, Block block1) {
        int x = Mth.floor(player.getX());
        int y = Mth.floor(player.getY());
        int z = Mth.floor(player.getZ());
        if (block1.equals(Blocks.AIR)) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    BlockPos pos1 = new BlockPos(x + i, y, z + j);
                    BlockPos pos2 = new BlockPos(x + i, y - 1, z + j);
                    if (level.getBlockState(pos1).is(BlockTags.SNOW) || level.getBlockState(pos2).is(BlockTags.SNOW)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package com.linngdu664.bsf3lite.item.weapon;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.network.to_server.AmmoTypePayload;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Iterator;
import java.util.LinkedHashSet;

public abstract class AbstractBSFWeaponItem extends Item {
    private final int typeFlag;
    private final LinkedHashSet<Item> launchOrder = new LinkedHashSet<>();   // client only
//    private ItemStack prevAmmoItemStack = Items.AIR.getDefaultInstance();      // client only
//    private ItemStack currentAmmoItemStack = Items.AIR.getDefaultInstance();   // client only
//    private ItemStack nextAmmoItemStack = Items.AIR.getDefaultInstance();      // client only

    private ItemStack prevAmmoItemStack = null;      // client only
    private ItemStack currentAmmoItemStack = null;   // client only
    private ItemStack nextAmmoItemStack = null;      // client only

    public AbstractBSFWeaponItem(String id, int durability, Rarity rarity, int flag) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .stacksTo(1)
                .durability(durability)
                .rarity(rarity)
                .repairable(Items.IRON_INGOT)
                .enchantable(25));
        this.typeFlag = flag;
    }

    public abstract ILaunchAdjustment getLaunchAdjustment(float damageDropRate, Item snowball);

    public abstract boolean isAllowBulkedSnowball();

    //Rewrite vanilla "shootFromRotation" method to remove the influence of player's velocity.
    protected void BSFShootFromRotation(Projectile projectile, float pX, float pY, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        float f1 = -Mth.sin(pX * Mth.DEG_TO_RAD);
        float f2 = Mth.cos(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        projectile.shoot(f, f1, f2, pVelocity, pInaccuracy);
    }

    protected void consumeAmmo(ItemStack itemStack, Player player) {
        if (!player.getAbilities().instabuild) {
            if (itemStack.getItem() instanceof SnowballTankItem) {
                if (!itemStack.has(DataComponents.UNBREAKABLE)) {
                    itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                    if (itemStack.getDamageValue() == itemStack.getMaxDamage()) {
                        itemStack.remove(DataComponentRegistry.AMMO_ITEM);
                    }
                }
            } else {
                itemStack.shrink(1);
                if (itemStack.isEmpty()) {
                    player.getInventory().removeItem(itemStack);
                }
            }
        }
    }

    protected AbstractBSFSnowballEntity ItemToEntity(ItemStack itemStack, Player player, Level level, ILaunchAdjustment launchAdjustment) {
        Item item = itemStack.getItem();
        RegionData region = itemStack.get(DataComponentRegistry.REGION.get());
        if (item instanceof SnowballTankItem) {
            item = itemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
        }
        if (item instanceof AbstractBSFSnowballItem snowball) {
            return snowball.getCorrespondingEntity(level, player, launchAdjustment, region);
        }
        return null;
    }

    public void inventoryTickInClient(ItemStack pStack, Player player, int pSlotId) {
        if (this == player.getMainHandItem().getItem() || this == player.getOffhandItem().getItem()) {
            Inventory inventory = player.getInventory();
            int k = inventory.getContainerSize();
            Object2IntOpenHashMap<Item> hashMap = new Object2IntOpenHashMap<>();
            for (int i = 0; i < k; i++) {
                ItemStack itemStack = inventory.getItem(i);
                Item item = itemStack.getItem();
                if (item instanceof SnowballTankItem && itemStack.has(DataComponentRegistry.AMMO_ITEM)) {
                    AbstractBSFSnowballItem snowball = (AbstractBSFSnowballItem) itemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
                    if ((typeFlag & snowball.getTypeFlag()) != 0) {
                        hashMap.put(snowball, hashMap.getOrDefault(snowball, 0) + itemStack.getMaxDamage() - itemStack.getDamageValue());
                    }
                } else if (isAllowBulkedSnowball() && item instanceof AbstractBSFSnowballItem snowball && (typeFlag & snowball.getTypeFlag()) != 0) {
                    hashMap.put(snowball, hashMap.getOrDefault(snowball, 0) + itemStack.getCount());
                }
            }
            launchOrder.addAll(hashMap.keySet());
            launchOrder.removeIf(item -> !hashMap.containsKey(item));
            modifyOrder(player, launchOrder);
            if (launchOrder.isEmpty()) {
                prevAmmoItemStack = Items.AIR.getDefaultInstance();
                currentAmmoItemStack = Items.AIR.getDefaultInstance();
                nextAmmoItemStack = Items.AIR.getDefaultInstance();
            } else if (launchOrder.size() == 1) {
                prevAmmoItemStack = Items.AIR.getDefaultInstance();
                currentAmmoItemStack = new ItemStack(launchOrder.getFirst(), hashMap.getOrDefault(launchOrder.getFirst(), 0));
                nextAmmoItemStack = Items.AIR.getDefaultInstance();
            } else {
                prevAmmoItemStack = new ItemStack(launchOrder.getLast(), hashMap.getOrDefault(launchOrder.getLast(), 0));
                currentAmmoItemStack = new ItemStack(launchOrder.getFirst(), hashMap.getOrDefault(launchOrder.getFirst(), 0));
                Iterator<Item> iterator = launchOrder.iterator();
                iterator.next();
                Item nextItem = iterator.next();
                nextAmmoItemStack = new ItemStack(nextItem, hashMap.getOrDefault(nextItem, 0));
            }
            Item newItem = currentAmmoItemStack.getItem();
            if (!newItem.equals(pStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item())) {
                ClientPacketDistributor.sendToServer(new AmmoTypePayload(newItem, pSlotId));
            }
        }
    }

    protected void modifyOrder(Player player, LinkedHashSet<Item> launchOrder) {

    }

    public ItemStack getAmmo(Player player, ItemStack weaponItemStack) {
        Item ammoItem = weaponItemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
        if (ammoItem == Items.AIR) {
            return null;
        }
        Inventory inventory = player.getInventory();
        int k = inventory.getContainerSize();
        for (int i = 0; i < k; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack.getItem() instanceof SnowballTankItem && itemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item() == ammoItem) {
                RegionData region = itemStack.get(DataComponentRegistry.REGION);
                if (region == null || region.inRegion(player.position())) {
                    return itemStack;
                }
            }
        }
        if (isAllowBulkedSnowball()) {
            for (int i = 0; i < k; i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack.getItem() instanceof AbstractBSFSnowballItem snowball && snowball == ammoItem) {
                    RegionData region = itemStack.get(DataComponentRegistry.REGION);
                    if (region == null || region.inRegion(player.position())) {
                        return itemStack;
                    }
                }
            }
        }
        return null;
    }

    public LinkedHashSet<Item> getLaunchOrder() {
        return launchOrder;
    }

    public ItemStack getPrevAmmoItemStack() {
        if (prevAmmoItemStack == null) {
            return Items.AIR.getDefaultInstance();
        }
        return prevAmmoItemStack;
    }

    public ItemStack getCurrentAmmoItemStack() {
        if (currentAmmoItemStack == null) {
            return Items.AIR.getDefaultInstance();
        }
        return currentAmmoItemStack;
    }

    public ItemStack getNextAmmoItemStack() {
        if (nextAmmoItemStack == null) {
            return Items.AIR.getDefaultInstance();
        }
        return nextAmmoItemStack;
    }

    public int getTypeFlag() {
        return typeFlag;
    }
}

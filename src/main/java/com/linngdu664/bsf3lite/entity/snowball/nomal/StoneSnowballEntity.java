package com.linngdu664.bsf3lite.entity.snowball.nomal;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class StoneSnowballEntity extends AbstractNormalSnowballEntity {
    public StoneSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(5));
    }

    public StoneSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.STONE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.STONE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(2).basicBlazeDamage(4), region);
    }

    public StoneSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.STONE_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.STONE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(2).basicBlazeDamage(4).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.STONE_SNOWBALL.get();
    }
}

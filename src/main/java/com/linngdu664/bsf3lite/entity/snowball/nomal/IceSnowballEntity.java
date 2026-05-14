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

public class IceSnowballEntity extends AbstractNormalSnowballEntity {
    public IceSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(4).basicBlazeDamage(7).basicFrozenTicks(40));
    }

    public IceSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.ICE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.ICE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(6).basicFrozenTicks(40), region);
    }

    public IceSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.ICE_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.ICE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(6).basicFrozenTicks(40).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.ICE_SNOWBALL.get();
    }
}

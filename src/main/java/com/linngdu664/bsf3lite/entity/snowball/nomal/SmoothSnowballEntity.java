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

public class SmoothSnowballEntity extends AbstractNormalSnowballEntity {
    public SmoothSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties());
    }

    public SmoothSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.SMOOTH_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.SMOOTH_SNOWBALL.toStack(), new BSFSnowballEntityProperties(), region);
    }

    public SmoothSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.SMOOTH_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.SMOOTH_SNOWBALL.toStack(), new BSFSnowballEntityProperties().applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.SMOOTH_SNOWBALL.get();
    }
}

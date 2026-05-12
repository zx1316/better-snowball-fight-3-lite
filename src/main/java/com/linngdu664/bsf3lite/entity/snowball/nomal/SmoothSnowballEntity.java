package com.linngdu664.bsf3lite.entity.snowball.nomal;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegister;
import com.linngdu664.bsf3lite.registry.ItemRegister;
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
        super(EntityRegister.SMOOTH_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegister.SMOOTH_SNOWBALL.toStack(), new BSFSnowballEntityProperties(), region);
    }

    public SmoothSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegister.SMOOTH_SNOWBALL.get(), pShooter, pLevel, ItemRegister.SMOOTH_SNOWBALL.toStack(), new BSFSnowballEntityProperties().applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegister.SMOOTH_SNOWBALL.get();
    }
}

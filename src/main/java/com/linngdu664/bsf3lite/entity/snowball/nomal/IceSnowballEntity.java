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
        super(pEntityType, pLevel, makeProperties());
    }

    public IceSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.ICE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.ICE_SNOWBALL.toStack(),
                makeProperties().aliveRange(region));
    }

    public IceSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.ICE_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.ICE_SNOWBALL.toStack(),
                makeProperties().applyAdjustment(launchAdjustment).aliveRange(region));
    }

    private static BSFSnowballEntityProperties makeProperties() {
        return new BSFSnowballEntityProperties().damage(4).blazeDamage(7).frozenTicks(40);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.ICE_SNOWBALL.get();
    }
}

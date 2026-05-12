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
import org.jetbrains.annotations.NotNull;

public class IceSnowballEntity extends AbstractNormalSnowballEntity {
    public IceSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(4).basicBlazeDamage(7).basicFrozenTicks(40));
    }

    public IceSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegister.ICE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegister.ICE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(6).basicFrozenTicks(40), region);
    }

    public IceSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegister.ICE_SNOWBALL.get(), pShooter, pLevel, ItemRegister.ICE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(6).basicFrozenTicks(40).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegister.ICE_SNOWBALL.get();
    }
}

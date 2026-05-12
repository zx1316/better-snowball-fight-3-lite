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

public class StoneSnowballEntity extends AbstractNormalSnowballEntity {
    public StoneSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(5));
    }

    public StoneSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegister.STONE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegister.STONE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(2).basicBlazeDamage(4), region);
    }

    public StoneSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegister.STONE_SNOWBALL.get(), pShooter, pLevel, ItemRegister.STONE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(2).basicBlazeDamage(4).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegister.STONE_SNOWBALL.get();
    }
}

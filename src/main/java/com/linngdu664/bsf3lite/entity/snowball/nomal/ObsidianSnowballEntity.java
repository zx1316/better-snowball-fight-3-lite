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

public class ObsidianSnowballEntity extends AbstractNormalSnowballEntity {
    public ObsidianSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(6).basicBlazeDamage(8));
    }

    public ObsidianSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegister.OBSIDIAN_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegister.OBSIDIAN_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(6).basicBlazeDamage(8), region);
    }

    public ObsidianSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegister.OBSIDIAN_SNOWBALL.get(), pShooter, pLevel, ItemRegister.OBSIDIAN_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(6).basicBlazeDamage(8).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegister.OBSIDIAN_SNOWBALL.get();
    }
}

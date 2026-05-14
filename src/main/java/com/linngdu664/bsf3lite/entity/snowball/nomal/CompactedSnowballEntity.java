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

public class CompactedSnowballEntity extends AbstractNormalSnowballEntity {
    public CompactedSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicPunch(2));
    }

    public CompactedSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.COMPACTED_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.COMPACTED_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicPunch(2), region);
    }

    public CompactedSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.COMPACTED_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.COMPACTED_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicPunch(2).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.COMPACTED_SNOWBALL.get();
    }
}

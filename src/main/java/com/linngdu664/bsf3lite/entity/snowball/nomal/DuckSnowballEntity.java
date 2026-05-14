package com.linngdu664.bsf3lite.entity.snowball.nomal;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class DuckSnowballEntity extends AbstractBSFSnowballEntity {
    public DuckSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties());
    }

    public DuckSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.DUCK_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.DUCK_SNOWBALL.toStack(), new BSFSnowballEntityProperties(), region);
    }

    public DuckSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.DUCK_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.DUCK_SNOWBALL.toStack(), new BSFSnowballEntityProperties().applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        Level level = level();
        if (!level.isClientSide() && !isCaught) {
            discard();
            level.playSound(null, getX(), getY(), getZ(), SoundRegistry.DUCK.get(), SoundSource.NEUTRAL, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.DUCK_SNOWBALL.get();
    }
}

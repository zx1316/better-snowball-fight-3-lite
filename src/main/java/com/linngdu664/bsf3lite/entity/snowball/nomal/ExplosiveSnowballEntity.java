package com.linngdu664.bsf3lite.entity.snowball.nomal;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ExplosiveSnowballEntity extends AbstractNormalSnowballEntity {
    public ExplosiveSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(5));
    }

    public ExplosiveSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.EXPLOSIVE_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.EXPLOSIVE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(5), region);
    }

    public ExplosiveSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.EXPLOSIVE_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.EXPLOSIVE_SNOWBALL.toStack(), new BSFSnowballEntityProperties().basicDamage(3).basicBlazeDamage(5).applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide() && !isCaught) {
            Vec3 location = BSFCommonUtil.getRealHitPosOnMoveVecWithHitResult(this, pResult);
            handleExplosion(1.5F, location);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.EXPLOSIVE_SNOWBALL.get();
    }
}

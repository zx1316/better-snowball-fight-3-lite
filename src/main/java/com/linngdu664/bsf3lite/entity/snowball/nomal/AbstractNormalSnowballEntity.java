package com.linngdu664.bsf3lite.entity.snowball.nomal;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.item.component.RegionData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNormalSnowballEntity extends AbstractBSFSnowballEntity {
    public AbstractNormalSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pLevel, pProperties);
    }

    public AbstractNormalSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel, ItemStack itemStack, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pX, pY, pZ, pLevel, itemStack, pProperties);
    }

    public AbstractNormalSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, LivingEntity pShooter, Level pLevel, ItemStack itemStack, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pShooter, pLevel, itemStack, pProperties);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide()) {
            this.discard();
        }
    }
}

package com.linngdu664.bsf3lite.entity.snowball.special;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AbstractSnowStorageSnowballEntity extends AbstractConstructSnowballEntity {
    protected int snowStock = 0;

    public AbstractSnowStorageSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel, int duration) {
        super(pEntityType, pLevel, duration, new BSFSnowballEntityProperties().canBeCaught(false));
    }

    public AbstractSnowStorageSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, LivingEntity pShooter, Level pLevel, ItemStack itemStack, ILaunchAdjustment launchAdjustment, int snowStock, int duration, RegionData region) {
        super(pEntityType, pShooter, pLevel, itemStack, duration, new BSFSnowballEntityProperties().canBeCaught(false).applyAdjustment(launchAdjustment), region);
        this.snowStock = snowStock;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("SnowStock", snowStock);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        snowStock = input.getIntOr("SnowStock", 0);
    }

    @Override
    protected void placeAndRecordBlock(Level level, BlockPos blockPos) {
        if (!level.isClientSide() && (getRegion() == null || getRegion().inRegion(blockPos)) && level.getBlockState(blockPos).canBeReplaced()) {
            level.setBlock(blockPos, BlockRegister.LOOSE_SNOW_BLOCK.get().defaultBlockState(), 3);
            snowStock--;
            allBlock.push(blockPos);
        }
    }
}

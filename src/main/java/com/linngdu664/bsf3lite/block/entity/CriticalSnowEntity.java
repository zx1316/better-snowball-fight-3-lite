package com.linngdu664.bsf3lite.block.entity;

import com.linngdu664.bsf3lite.registry.BlockEntityRegistry;
import com.linngdu664.bsf3lite.registry.BlockRegistry;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

public class CriticalSnowEntity extends BlockEntity {
    private int targetAge = BSFCommonUtil.staticRandInt(100, 140);
    private int age;

    public CriticalSnowEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CRITICAL_SNOW.get(), pPos, pBlockState);
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (!level.isClientSide()) {
            CriticalSnowEntity criticalSnowEntity = (CriticalSnowEntity) blockEntity;
            if (criticalSnowEntity.age < criticalSnowEntity.targetAge) {
                criticalSnowEntity.age++;
                criticalSnowEntity.setChanged();
            } else {
                criticalSnowEntity.setRemoved();
                BlockState snow = Blocks.SNOW.defaultBlockState();
                if (level.getBlockState(pos).canBeReplaced() && snow.canSurvive(level, pos) && !level.getBlockState(pos.below()).getBlock().getName().getString().equals(BlockRegistry.LOOSE_SNOW_BLOCK.get().getName().getString())) {
                    level.setBlockAndUpdate(pos, snow);
                } else {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SNOW_STEP, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, pos.getX(), pos.getY(), pos.getZ(), 5, 0, 0, 0, 0.12);
            }
        }
    }

    public void suicide() {
        this.age = this.targetAge;
        this.setChanged();
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        age = input.getIntOr("age", 0);
        targetAge = input.getIntOr("target_age", 0);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("age", age);
        output.putInt("target_age", targetAge);
    }
}

package com.linngdu664.bsf3lite.block;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.registry.BlockRegistry;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SmartSnowBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<SmartSnowBlock> CODEC = simpleCodec(SmartSnowBlock::new);
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private static final Predicate<BlockState> PUMPKINS_PREDICATE = (p_51396_) -> p_51396_ != null && p_51396_.is(BlockRegistry.SMART_SNOW_BLOCK.get());
    private BlockPattern snowGolemFull;

    public SmartSnowBlock(Properties properties) {
        super(properties.setId(ResourceKey.create(Registries.BLOCK, Main.makeMyIdentifier("smart_snow_block"))));
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public SmartSnowBlock() {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Main.makeMyIdentifier("smart_snow_block")))
                .mapColor(MapColor.SNOW).strength(0.5F).sound(SoundType.SNOW));
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer instanceof Player player) {
            BlockPattern.BlockPatternMatch blockPatternMatch = getOrCreateSnowGolemFull().find(pLevel, pPos);
            if (blockPatternMatch != null) {
                for (int i = 0; i < getOrCreateSnowGolemFull().getHeight(); ++i) {
                    BlockInWorld blockInWorld = blockPatternMatch.getBlock(0, i, 0);
                    pLevel.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    pLevel.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
                }
                BSFSnowGolemEntity snowGolem = EntityRegistry.BSF_SNOW_GOLEM.get().create(pLevel, EntitySpawnReason.TRIGGERED);
                RandomSource randomSource = pLevel.getRandom();
                snowGolem.setOwnerReference(EntityReference.of(player));
                snowGolem.setAliveRange(pStack.get(DataComponentRegistry.REGION));
                snowGolem.setStyle((byte) (randomSource.nextInt(0, AbstractBSFSnowGolemEntity.STYLE_NUM)));
                snowGolem.setPredictMotion(randomSource.nextFloat() < 0.1f);
                BlockPos blockPos = blockPatternMatch.getBlock(0, 2, 0).getPos();
                snowGolem.snapTo(blockPos.getX() + 0.5, blockPos.getY() + 0.05, blockPos.getZ() + 0.5, 0.0F, 0.0F);
                pLevel.addFreshEntity(snowGolem);
                for (ServerPlayer serverplayer : pLevel.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, snowGolem);
                }
                for (int l = 0; l < getOrCreateSnowGolemFull().getHeight(); ++l) {
                    BlockInWorld blockInWorld = blockPatternMatch.getBlock(0, l, 0);
                    pLevel.setBlockAndUpdate(blockInWorld.getPos(), Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (snowGolemFull == null) {
            snowGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return snowGolemFull;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}

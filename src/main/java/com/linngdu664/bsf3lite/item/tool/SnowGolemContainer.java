package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SnowGolemContainer extends Item {
    public SnowGolemContainer() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("snow_golem_container")))
                .stacksTo(1));
    }

    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        ItemStack itemStack = pContext.getItemInHand();
        Player player = pContext.getPlayer();
        if (itemStack.has(DataComponentRegistry.SNOW_GOLEM_DATA)) {
            if (!level.isClientSide()) {
                BSFSnowGolemEntity snowGolem = EntityRegistry.BSF_SNOW_GOLEM.get().create(level, EntitySpawnReason.TRIGGERED);
                if (snowGolem != null) {
                    try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(snowGolem.problemPath(), Main.LOGGER)) {
                        ValueInput valueInput = TagValueInput.create(reporter, snowGolem.registryAccess(), itemStack.get(DataComponentRegistry.SNOW_GOLEM_DATA));
                        snowGolem.load(valueInput);
                        BlockPos blockPos = pContext.getClickedPos();
                        snowGolem.snapTo(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0.0F, 0.0F);
                        snowGolem.setOwnerReference(EntityReference.of(player));
                        level.addFreshEntity(snowGolem);
                        itemStack.remove(DataComponentRegistry.SNOW_GOLEM_DATA);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_PLACE, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snow_golem_container.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

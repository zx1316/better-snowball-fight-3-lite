package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
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

public class CreativeSnowGolemToolItem extends Item {
    public CreativeSnowGolemToolItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("creative_snow_golem_tool")))
                .rarity(Rarity.EPIC)
                .stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide()) {
            ItemStack stack = pContext.getItemInHand();
            if (stack.has(DataComponentRegistry.SNOW_GOLEM_DATA)) {
                BSFSnowGolemEntity snowGolem = EntityRegistry.BSF_SNOW_GOLEM.get().create(level, EntitySpawnReason.TRIGGERED);
                if (snowGolem != null) {
                    try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(snowGolem.problemPath(), Main.LOGGER)) {
                        ValueInput valueInput = TagValueInput.create(reporter, snowGolem.registryAccess(), stack.get(DataComponentRegistry.SNOW_GOLEM_DATA));
                        snowGolem.load(valueInput);
                        BlockPos blockPos = pContext.getClickedPos();
                        snowGolem.snapTo(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0.0F, 0.0F);
                        level.addFreshEntity(snowGolem);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("creative_snow_golem_tool.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("creative_snow_golem_tool1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("creative_snow_golem_tool2.tooltip", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("creative_snow_golem_tool3.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

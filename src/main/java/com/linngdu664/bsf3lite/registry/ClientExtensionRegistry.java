package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.IceSkatesModel;
import com.linngdu664.bsf3lite.client.model.SnowFallBootsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.Map;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class ClientExtensionRegistry {
    private static final HumanoidModel.ArmPose BSF_WEAPON = HumanoidModel.ArmPose.valueOf("BSF3LITE_WEAPON");

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions weaponExtensions = new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NonNull LivingEntity entity, @NonNull InteractionHand hand, @NonNull ItemStack itemStack) {
                return BSF_WEAPON;
            }
        };
        event.registerItem(weaponExtensions, ItemRegistry.SNOWBALL_SHOTGUN);
        event.registerItem(weaponExtensions, ItemRegistry.SCULK_SNOWBALL_LAUNCHER);
        event.registerItem(weaponExtensions, ItemRegistry.IMPLOSION_SNOWBALL_CANNON);

        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> cachedModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
                if (cachedModel == null) {
                    EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
                    cachedModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new IceSkatesModel(entityModelSet.bakeLayer(IceSkatesModel.LEFT_LAYER)).bone,
                            "right_leg", new IceSkatesModel(entityModelSet.bakeLayer(IceSkatesModel.RIGHT_LAYER)).bone,
                            "head", new ModelPart(Collections.emptyList(), Map.of("hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()))),
                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                }
                return cachedModel;
            }
        }, ItemRegistry.ICE_SKATES);
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> cachedModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
                if (cachedModel == null) {
                    EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
                    cachedModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new SnowFallBootsModel(entityModelSet.bakeLayer(SnowFallBootsModel.LEFT_LAYER)).bone,
                            "right_leg", new SnowFallBootsModel(entityModelSet.bakeLayer(SnowFallBootsModel.RIGHT_LAYER)).bone,
                            "head", new ModelPart(Collections.emptyList(), Map.of("hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()))),
                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                }
                return cachedModel;
            }
        }, ItemRegistry.SNOW_FALL_BOOTS);
    }
}

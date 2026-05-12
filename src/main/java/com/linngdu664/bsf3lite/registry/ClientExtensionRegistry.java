package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.IceSkatesModel;
import com.linngdu664.bsf3lite.client.model.SnowFallBootsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
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
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions weaponExtensions = new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NonNull LivingEntity entity, @NonNull InteractionHand hand, @NonNull ItemStack itemStack) {
                return HumanoidModel.ArmPose.valueOf("BSF_WEAPON");
            }
        };
        event.registerItem(weaponExtensions, ItemRegister.SNOWBALL_SHOTGUN);
        event.registerItem(weaponExtensions, ItemRegister.SCULK_SNOWBALL_LAUNCHER);
        event.registerItem(weaponExtensions, ItemRegister.IMPLOSION_SNOWBALL_CANNON);

        event.registerItem(new IClientItemExtensions() {
            @Override
            public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
                return new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                        "left_leg", new IceSkatesModel(Minecraft.getInstance().getEntityModels().bakeLayer(IceSkatesModel.LAYER_LOCATION)).bone,
                        "right_leg", new IceSkatesModel(Minecraft.getInstance().getEntityModels().bakeLayer(IceSkatesModel.LAYER_LOCATION)).bone,
                        "head", new ModelPart(Collections.emptyList(), Map.of("hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()))),
                        "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
            }
        }, ItemRegister.ICE_SKATES);
        event.registerItem(new IClientItemExtensions() {
            @Override
            public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
                return new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                        "left_leg", new SnowFallBootsModel(Minecraft.getInstance().getEntityModels().bakeLayer(SnowFallBootsModel.LAYER_LOCATION)).bone,
                        "right_leg", new SnowFallBootsModel(Minecraft.getInstance().getEntityModels().bakeLayer(SnowFallBootsModel.LAYER_LOCATION)).bone,
                        "head", new ModelPart(Collections.emptyList(), Map.of("hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()))),
                        "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
            }
        }, ItemRegister.SNOW_FALL_BOOTS);
    }
}

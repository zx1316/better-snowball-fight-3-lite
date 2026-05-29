package com.linngdu664.bsf3lite.client.renderer.entity;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.BSFSnowGolemModel;
import com.linngdu664.bsf3lite.client.renderer.entity.layers.BSFSnowGolemHoldItemLayer;
import com.linngdu664.bsf3lite.client.renderer.entity.state.BSFSnowGolemRenderState;
import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class BSFSnowGolemRenderer extends MobRenderer<AbstractBSFSnowGolemEntity, BSFSnowGolemRenderState, BSFSnowGolemModel> {
    public BSFSnowGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new BSFSnowGolemModel(context.bakeLayer(BSFSnowGolemModel.LAYER_LOCATION)), 0.7f);
        addLayer(new BSFSnowGolemHoldItemLayer(this));
    }

    @Override
    public BSFSnowGolemRenderState createRenderState() {
        return new BSFSnowGolemRenderState();
    }

    @Override
    public void extractRenderState(AbstractBSFSnowGolemEntity entity, BSFSnowGolemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.weapon = entity.getWeapon();
        state.style = entity.getStyle();
        state.ammo = entity.getAmmo();
        state.weaponAngle = entity.getWeaponAng();
        state.golem = entity;
    }

    @Override
    public Identifier getTextureLocation(BSFSnowGolemRenderState state) {
        return switch (state.style) {
            case 0 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_1.png");
            case 1 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_2.png");
            case 2 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_3.png");
            case 3 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_4.png");
            case 4 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_5.png");
            case 5 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_6.png");
            case 6 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_7.png");
            case 7 -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_8.png");
            default -> Main.makeMyIdentifier("textures/entity/bsf_snow_golem/bsf_snow_golem_9.png");
        };
    }
}

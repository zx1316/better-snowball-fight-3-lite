package com.linngdu664.bsf3lite.client.renderer.entity;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.BSFDummyModel;
import com.linngdu664.bsf3lite.client.renderer.entity.state.BSFDummyRenderState;
import com.linngdu664.bsf3lite.entity.BSFDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class BSFDummyRenderer extends MobRenderer<BSFDummyEntity, BSFDummyRenderState, BSFDummyModel> {
    public BSFDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new BSFDummyModel(context.bakeLayer(BSFDummyModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public BSFDummyRenderState createRenderState() {
        return new BSFDummyRenderState();
    }

    @Override
    public void extractRenderState(BSFDummyEntity entity, BSFDummyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.style = entity.getStyle();
    }

    @Override
    public Identifier getTextureLocation(BSFDummyRenderState state) {
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

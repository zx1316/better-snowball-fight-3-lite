package com.linngdu664.bsf3lite.client.renderer.entity;

import com.linngdu664.bsf3lite.registry.ItemRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;

public class SculkSnowballRenderer<T extends Entity & ItemSupplier> extends ThrownItemRenderer<T> {
    public SculkSnowballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(T entity, ThrownItemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        this.itemModelResolver.updateForNonLiving(state.item, ItemRegistry.SCULK_SNOWBALL.toStack(), ItemDisplayContext.GROUND, entity);
    }
}

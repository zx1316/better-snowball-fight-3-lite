package com.linngdu664.bsf3lite.client.renderer.entity.state;

import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class BSFSnowGolemRenderState extends LivingEntityRenderState {
    public ItemStack weapon;
    public ItemStack ammo;
    public int weaponAngle;
    public byte style;
    public AbstractBSFSnowGolemEntity golem;
}

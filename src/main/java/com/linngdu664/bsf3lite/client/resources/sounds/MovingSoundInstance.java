package com.linngdu664.bsf3lite.client.resources.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class MovingSoundInstance extends AbstractTickableSoundInstance {
    private final Entity entity;

    public MovingSoundInstance(Entity entity, SoundEvent soundEvent, boolean looping) {
        super(soundEvent, SoundSource.NEUTRAL, entity.level().getRandom());
        this.entity = entity;
        this.looping = looping;
        this.delay = 0;
        this.volume = 1F;
    }

    @Override
    public void tick() {
        if (entity.isRemoved()) {
            stop();
        } else {
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }
    }

    public void requestStop() {
        stop();
    }

    @Override
    public boolean canPlaySound() {
        return !isStopped();
    }

    public boolean isBoundTo(Entity entity) {
        return this.entity == entity;
    }
}

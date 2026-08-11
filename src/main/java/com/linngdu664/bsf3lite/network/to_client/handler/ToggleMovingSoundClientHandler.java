package com.linngdu664.bsf3lite.network.to_client.handler;

import com.linngdu664.bsf3lite.client.resources.sounds.MovingSoundInstance;
import com.linngdu664.bsf3lite.network.to_client.ToggleMovingSoundPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ToggleMovingSoundClientHandler {
    private static final Map<LoopingSoundKey, MovingSoundInstance> LOOPING_SOUNDS = new HashMap<>();

    public static void handlePayload(ToggleMovingSoundPayload payload) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        LoopingSoundKey key = LoopingSoundKey.from(payload);
        if (payload.flag() == ToggleMovingSoundPayload.STOP_LOOP) {
            stopLoopingSound(key, soundManager);
            return;
        }

        Entity entity = level.getEntity(payload.entityId());
        if (entity != null) {
            if (payload.flag() == ToggleMovingSoundPayload.PLAY_LOOP) {
                MovingSoundInstance existing = LOOPING_SOUNDS.get(key);
                if (existing == null || existing.isStopped() || !existing.isBoundTo(entity)) {
                    if (existing != null) {
                        stopLoopingSound(key, soundManager);
                    }
                    MovingSoundInstance soundInstance = new MovingSoundInstance(entity, payload.soundEvent(), true);
                    LOOPING_SOUNDS.put(key, soundInstance);
                    soundManager.queueTickingSound(soundInstance);
                }
            } else {
                MovingSoundInstance soundInstance = new MovingSoundInstance(entity, payload.soundEvent(), false);
                soundManager.queueTickingSound(soundInstance);
            }
        }
    }

    public static void stopAllLoopingSounds() {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        for (MovingSoundInstance soundInstance : LOOPING_SOUNDS.values()) {
            soundInstance.requestStop();
            soundManager.stop(soundInstance);
        }
        LOOPING_SOUNDS.clear();
    }

    private static void stopLoopingSound(LoopingSoundKey key, SoundManager soundManager) {
        MovingSoundInstance soundInstance = LOOPING_SOUNDS.remove(key);
        if (soundInstance != null) {
            soundInstance.requestStop();
            soundManager.stop(soundInstance);
        }
    }

    private record LoopingSoundKey(int entityId, Identifier soundId) {
        private static LoopingSoundKey from(ToggleMovingSoundPayload payload) {
            return new LoopingSoundKey(payload.entityId(), payload.soundEvent().location());
        }
    }
}

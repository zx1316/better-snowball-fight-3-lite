package com.linngdu664.bsf3lite.network.to_server;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AmmoTypePayload(Item ammo, int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AmmoTypePayload> TYPE = new CustomPacketPayload.Type<>(Main.makeMyIdentifier("ammo_type"));
    public static final StreamCodec<ByteBuf, AmmoTypePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, AmmoTypePayload::getItemName,
            ByteBufCodecs.VAR_INT, AmmoTypePayload::slot,
            AmmoTypePayload::makeAmmoTypePayload
    );

    private static AmmoTypePayload makeAmmoTypePayload(String itemName, int slot) {
        Identifier identifier = Identifier.tryParse(itemName);
        if (identifier == null) {
            return new AmmoTypePayload(Items.AIR, 1);
        }
        var itemOptional = BuiltInRegistries.ITEM.get(identifier);
        return itemOptional.map(itemReference -> new AmmoTypePayload(itemReference.value(), slot)).orElseGet(() -> new AmmoTypePayload(Items.AIR, 1));
    }

    public static void handleDataInServer(AmmoTypePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getInventory().getItem(payload.slot);
            if (itemStack.getItem() instanceof AbstractBSFWeaponItem) {
                itemStack.set(DataComponentRegister.AMMO_ITEM, new ItemData(payload.ammo));
            }
        });
    }

    private String getItemName() {
        return BuiltInRegistries.ITEM.getKey(ammo).toString();
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

package com.dylanpdx.retro64.attachments;

import com.dylanpdx.retro64.Retro64;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class retro64attachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Retro64.MOD_ID);

    // Serialization via codec
    public static final Supplier<AttachmentType<Boolean>> IS_MARIO = ATTACHMENT_TYPES.register(
            "is_mario", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );

    public static void register(IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }
}

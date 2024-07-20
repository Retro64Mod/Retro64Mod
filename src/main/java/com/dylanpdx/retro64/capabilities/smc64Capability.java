package com.dylanpdx.retro64.capabilities;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class smc64Capability {
    public static final Capability<smc64CapabilityInterface> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(smc64CapabilityInterface.class);
    }

    private smc64Capability() {
    }
}

package com.dylanpdx.retro64.capabilities;

import net.minecraft.nbt.CompoundTag;

public class smc64CapabilityImplementation implements smc64CapabilityInterface {

    private static final String NBT_KEY_ENABLED = "smc64_enabled";

    private boolean isEnabled;

    @Override
    public boolean getIsEnabled() {
        return isEnabled;
    }

    @Override
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_ENABLED, this.isEnabled);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isEnabled = nbt.getBoolean(NBT_KEY_ENABLED);
    }
}

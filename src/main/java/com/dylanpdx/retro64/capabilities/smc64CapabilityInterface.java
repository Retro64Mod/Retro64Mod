package com.dylanpdx.retro64.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface smc64CapabilityInterface extends INBTSerializable<CompoundTag> {

    boolean getIsEnabled();
    void setIsEnabled(boolean isEnabled);

}

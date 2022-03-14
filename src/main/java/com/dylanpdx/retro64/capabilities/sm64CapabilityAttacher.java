package com.dylanpdx.retro64.capabilities;

import com.dylanpdx.retro64.Retro64;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class sm64CapabilityAttacher {

    private static class sm64CapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Retro64.MOD_ID, "retro64capability");
        private final smc64CapabilityInterface backend = new smc64CapabilityImplementation();
        private final LazyOptional<smc64CapabilityInterface> optionalData = LazyOptional.of(() -> backend);


        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return smc64Capability.INSTANCE.orEmpty(cap, optionalData);
        }

        void invalidate(){
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event){
        final sm64CapabilityProvider provider = new sm64CapabilityProvider();
        event.addCapability(sm64CapabilityProvider.IDENTIFIER, provider);
    }

    private sm64CapabilityAttacher(){

    }

}

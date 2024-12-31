package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.networking.packets.McharPacket;
import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jline.utils.Log;

import java.io.IOException;
import java.util.logging.Logger;

public class ClientPayloadHandler {
    public static void handleMcharPacket(final McharPacket data, final IPayloadContext context) {
        var marioPlayer = context.player().level().getPlayerByUUID(data.player().getId());
        if (marioPlayer != null){
            marioPlayer.setPos(data.pos().x(),data.pos().y(),data.pos().z());
            if (data.model() == -1 && data.action() == -1){
                RemoteMCharHandler.mCharOff(marioPlayer);
                return;
            }
            RemoteMCharHandler.mCharOn(marioPlayer);
            try {
                var pos = new Vec3(data.pos().x(),data.pos().y(),data.pos().z());
                short animRotX = (short) data.animRot().x();
                short animRotY = (short) data.animRot().y();
                short animRotZ = (short) data.animRot().z();
                RemoteMCharHandler.updateMChar(marioPlayer, AnimInfo.deserialize(data.animInfo()),animRotX,animRotY,animRotZ,data.action(),data.model(),pos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

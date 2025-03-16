package com.dylanpdx.retro64.events;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.Utils;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.event.ControllerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;

/**
 * Events for the client side, if the user has the Controllable mod installed.
 */
public class clientControllerEvents {

    public static Vec2 input = new Vec2(0,0);

    public static ControllerEvents.UpdateMovement MoveEvent = new ControllerEvents.UpdateMovement() {
        @Override
        public boolean handle() {
            var controller = Controllable.getController();
            if (controller==null || !Utils.getIsMario(Minecraft.getInstance().player)) return false;
            float x = 0;
            float y = 0;
            x = controller.getLThumbStickXValue();
            y = controller.getLThumbStickYValue();
            // if x or y is between -0.01 and 0.01, set it to 0
            if (!(x<-0.1f || x>0.1f)) x = 0;
            if (!(y<-0.1f || y>0.1f)) y = 0;
            input =  new Vec2(-y, x);
            if (input.x == 1 || input.x == -1 || input.y == 1 || input.y == -1) {
                input=input.normalized();
            }
            return true;
        }
    };

    public static void register(){
        ControllerEvents.UPDATE_MOVEMENT.register(MoveEvent);
    }
}

package com.dylanpdx.retro64.events;


/**
 * Events for the client side, if the user has the Controllable mod installed.
 */
/*public class clientControllerEvents {

    public static Vec2 input = new Vec2(0,0);

    @SubscribeEvent
    public void onControllerMove(ControllerEvent.Move event) {
        var controller = event.getController();
        if (controller==null) return;
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
    }
}
*/
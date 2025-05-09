package com.dylanpdx.retro64;

public class FpsLimiter {
    private long lastTime = System.nanoTime();
    private long targetFps = 60;

    public FpsLimiter(long targetFps) {
        this.targetFps = targetFps;
    }

    // Returns true if this function was called too fast, false if the function was called on or after the target time
    public boolean isLimited() {
        long now = System.nanoTime();
        long elapsed = now - lastTime;
        long targetTime = 1000000000 / targetFps;
        if (elapsed < targetTime) {
            return true; // Called too fast
        }
        lastTime = System.nanoTime();
        return false;
    }
}

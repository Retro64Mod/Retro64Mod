package com.dylanpdx.retro64.sm64;

public enum SM64MCharActionFlags {
    ACT_FLAG_STATIONARY(1 <<  9),
    ACT_FLAG_MOVING(1 << 10),
    ACT_FLAG_AIR(1 << 11),
    ACT_FLAG_INTANGIBLE(1 << 12),
    ACT_FLAG_SWIMMING(1 << 13),
    ACT_FLAG_METAL_WATER(1 << 14),
    ACT_FLAG_SHORT_HITBOX(1 << 15),
    ACT_FLAG_RIDING_SHELL(1 << 16),
    ACT_FLAG_INVULNERABLE(1 << 17),
    ACT_FLAG_BUTT_OR_STOMACH_SLIDE(1 << 18),
    ACT_FLAG_DIVING(1 << 19),
    ACT_FLAG_ON_POLE(1 << 20),
    ACT_FLAG_HANGING(1 << 21),
    ACT_FLAG_IDLE(1 << 22),
    ACT_FLAG_ATTACKING(1 << 23),
    ACT_FLAG_ALLOW_VERTICAL_WIND_ACTION(1 << 24),
    ACT_FLAG_CONTROL_JUMP_HEIGHT(1 << 25),
    ACT_FLAG_ALLOW_FIRST_PERSON(1 << 26),
    ACT_FLAG_PAUSE_EXIT(1 << 27),
    ACT_FLAG_SWIMMING_OR_FLYING(1 << 28),
    ACT_FLAG_WATER_OR_TEXT(1 << 29),
    ACT_FLAG_THROWING(1 << 31);

    public final int value;

    private SM64MCharActionFlags(int value) {
        this.value = value;
    }

    public static String getAllFlags(int value) {
        StringBuilder sb = new StringBuilder();
        for(SM64MCharActionFlags flag : SM64MCharActionFlags.values()) {
            if((flag.value & value) != 0) {
                sb.append(flag.name()).append(" ");
            }
        }
        return sb.toString();
    }
}

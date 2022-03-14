package com.dylanpdx.retro64.sm64;

public enum SM64MCharStateFlags {
    MCHAR_NORMAL_CAP(0x00000001),
    MCHAR_VANISH_CAP(0x00000002),
    MCHAR_METAL_CAP(0x00000004),
    MCHAR_WING_CAP(0x00000008),
    MCHAR_CAP_ON_HEAD(0x00000010),
    MCHAR_CAP_IN_HAND(0x00000020),
    MCHAR_METAL_SHOCK(0x00000040),
    MCHAR_TELEPORTING(0x00000080),
    MCHAR_UNKNOWN_08(0x00000100),
    MCHAR_UNKNOWN_13(0x00002000),
    MCHAR_ACTION_SOUND_PLAYED(0x00010000),
    MCHAR_MCHAR_SOUND_PLAYED(0x00020000),
    MCHAR_UNKNOWN_18(0x00040000),
    MCHAR_PUNCHING(0x00100000),
    MCHAR_KICKING(0x00200000),
    MCHAR_TRIPPING(0x00400000),
    MCHAR_UNKNOWN_25(0x02000000),
    MCHAR_UNKNOWN_30(0x40000000),
    MCHAR_UNKNOWN_31(0x80000000);

    private final int value;

    private SM64MCharStateFlags(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getAllFlags(int value){
        StringBuilder sb = new StringBuilder();
        for(SM64MCharStateFlags flag : SM64MCharStateFlags.values()){
            if((flag.getValue() & value) == flag.getValue()){
                sb.append(flag.name()).append(" ");
            }
        }
        return sb.toString();
    }
}

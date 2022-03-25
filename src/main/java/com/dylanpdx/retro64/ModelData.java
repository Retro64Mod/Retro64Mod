package com.dylanpdx.retro64;

public enum ModelData {
    MCHAR("",""),
    LUIGI("Luigi","Twitter: @FluffaLuigi"),
    STEVE("Steve","Twitter: @sanikkat"),
    ALEX("Alex","Twitter: @sanikkat"),
    NECOARC("Neco-Arc","Sketchfab: @paperbandit");

    ModelData(String name,String credit){
        this.name = name;
        this.credit = credit;
    }

    public String getName(){
        return name;
    }

    public String getCredit(){
        return credit;
    }

    private final String name;
    private final String credit;
    public static final int modelCount=5;
}

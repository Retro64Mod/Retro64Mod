package com.dylanpdx.retro64;

public enum ModelData {
    MCHAR("","",0),
    LUIGI("Luigi","Twitter: @FluffaLuigi",1),
    STEVE("Steve","Twitter: @sanikkat",2),
    ALEX("Alex","Twitter: @sanikkat",3),
    NECOARC("Neco-Arc","Sketchfab: @paperbandit",4),
    VIBRI("Vibri","Models Resource: @Ratinight",5);

    ModelData(String name,String credit,int index){
        this.name = name;
        this.credit = credit;
        this.index = index;
    }

    public String getName(){
        return name;
    }

    public String getCredit(){
        return credit;
    }

    public int getIndex(){
        return index;
    }

    private final String name;
    private final String credit;
    private final int index;
    public static final int modelCount=6;
}

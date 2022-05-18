package com.dylanpdx.retro64;

public enum ModelData {
    MCHAR("","",0),
    LUIGI("Luigi","Twitter: @FluffaLuigi","https://twitter.com/FluffaLuigi",1),
    STEVE("Steve","Twitter: @sanikkat",2),
    ALEX("Alex","Twitter: @sanikkat",3),
    NECOARC("Neco-Arc","Sketchfab: @paperbandit","https://sketchfab.com/3d-models/neco-arc-8bcd385adec44fdf8ebfc63bcdf5b28c",4),
    VIBRI("Vibri","Models Resource: @Ratinight","https://www.models-resource.com/custom_edited/vibribboncustoms/model/48606/",5),
    SONIC("Classic Sonic","sm64pc.info: @kurobutt","https://sm64pc.info/forum/viewtopic.php?f=2&t=141",6);

    ModelData(String name,String credit,int index){
        this.name = name;
        this.credit = credit;
        this.index = index;
        this.creditURL=null;
    }

    ModelData(String name,String credit,String creditURL,int index){
        this.name = name;
        this.credit = credit;
        this.index = index;
        this.creditURL=creditURL; // currently unused, will be used for making credit clickable later
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
    private final String creditURL;
    private final int index;
    public static final int modelCount=7;
}

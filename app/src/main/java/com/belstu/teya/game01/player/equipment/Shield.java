package com.belstu.teya.game01.player.equipment;

import com.google.gson.internal.$Gson$Preconditions;

public class Shield extends Equpment {
    Double defPoint;

    public Double getDefPoint() {
        return defPoint;
    }
    public Shield(){
        super();
        defPoint = 0.;
        type = "shield";
        name = "NoShield";
    }



    public Shield(int ID,Integer Element, String Name, Double DefPoint, Integer Price){
        super(ID,Element,Name, Price);
        type = "shield";
        defPoint = DefPoint;
    }
}

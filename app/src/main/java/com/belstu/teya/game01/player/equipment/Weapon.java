package com.belstu.teya.game01.player.equipment;

public class Weapon extends Equpment{
    Double attPow;

    public Double getAttPow() {
        return attPow;
    }
    public Weapon(){
        super();
        attPow = 0.;
        type = "sword";
        name = "NoWeapon";
    }
    public Weapon (int ID,Integer Element, String Name, Double AttPow, Integer Price){
        super(ID ,Element,Name,Price);
        attPow = AttPow;
        type = "sword";
    }
}

package com.belstu.teya.game01.player.equipment;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Equpment implements Serializable {

    int id;

    public Boolean isEquipped;
    Integer elem;//0 - none, 1 - fire, 2 - water, 3 - elect//1 beat 3, 2 beat 1, 3 beat 2
    String name;
    Integer price;
    String type;//маленькими буквами на английском
    public String getName() {
        return name;
    }

    public Integer getElem() {
        return elem;
    }

    public Integer getPrice() {
        return price;
    }

    public String getType1() {
        return type;
    }

    public Equpment(){
        elem = 0;
    }
    public Equpment(int ID, Integer Element, String Name, Integer Price){
        id = ID;
        elem = Element;
        name = Name;
        price = Price;
    }

    public boolean equals(Equpment obj) {
        return name.equals(obj.name);
    }
}

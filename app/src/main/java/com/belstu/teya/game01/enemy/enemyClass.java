package com.belstu.teya.game01.enemy;

import android.util.Base64;

public class enemyClass {
    int id;

    String name;
    int money;
    Double HP;
    Double deffence;
    Double attack;
    Integer elem;
    Integer MinCooldown;
    public String ByteImage;


    public int getMoney() {
        return money;
    }

    public Integer getMinCooldown() {
        return MinCooldown;
    }

    public Double Attack(){
        return attack * (0.85 + Math.random()*1.15);
    }
    public String toString() {
        return name+"\nHP=" + HP ;
    }

    public String getName() {
        return name;
    }

    public Integer getElem() {
        return elem;
    }
    public double GotHarm(Double harm, Integer elemIn){
        harm = (harm - deffence ) * ElemInteraction(elemIn, elem);
        if (harm >0) {
            HP -= harm;
            return harm;
        }
        else
            return 0;
    }
    public Boolean IsDead(){
        if(HP>0)
            return false;
        else return true;
    }
    Double ElemInteraction(Integer curr, Integer elem2) {
        Double result = 1.0;
        switch (curr)
        {
            case 0:
                result =  1.0;
                break;
            case 1:
                if(elem2 == 3)
                    result=  1.7;
                else
                    result = 0.7;
                break;
            case 2:
                if(elem2 == 1)
                    result=  1.7;
                else
                    result = 0.7;
                break;
            case 3:
                if(elem2 == 2)
                    result=  1.7;
                else
                    result = 0.7;
        }
        return  result;
    }
    public enemyClass(){
        id = 0;
        name = "Placeholder";
        money = 34;
        HP = 100.;
        deffence = 0.;
        attack = 10.;
        elem = 0;
        MinCooldown = 1;
    }
    public enemyClass(int ID, String nameIn, int moneyIn, Double HPInm, Double deffenceIn, Double attackIn, int elemIn, int miniCooldownIn){
        id = ID;
        name = nameIn;
        money = moneyIn;
        HP = HPInm;
        deffence = deffenceIn;
        attack = attackIn;
        elem = elemIn;
        MinCooldown = miniCooldownIn;
    }
    public enemyClass(int ID, String nameIn, int moneyIn, Double HPInm, Double deffenceIn, Double attackIn, int elemIn, int miniCooldownIn, String img){
        id = ID;
        name = nameIn;
        money = moneyIn;
        HP = HPInm;
        deffence = deffenceIn;
        attack = attackIn;
        elem = elemIn;
        MinCooldown = miniCooldownIn;
        ByteImage = img;
    }
}

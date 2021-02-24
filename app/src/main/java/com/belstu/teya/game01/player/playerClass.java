package com.belstu.teya.game01.player;

import com.belstu.teya.game01.player.equipment.Equpment;
import com.belstu.teya.game01.player.equipment.Shield;
import com.belstu.teya.game01.player.equipment.Weapon;

import java.io.Serializable;
import java.util.ArrayList;

public class playerClass implements Serializable {
    int money;
    Double attack;
    Double deffence;
    Double HP;
    Double HPrestoreAbility;
    Weapon weapon;
    Shield shield;

    public Shield getShield() {
        return shield;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public String toString() {
        return "player\nHP=" + HP +
                "\nweapon:" + weapon.getName() +
                "\nshield:" + shield.getName();
    }

    public int AddMoney(int in){
        money +=in;
        return in;
    }
    public Boolean SpendMoney(int in){
        if (in<=money)
        {
            money -=in;
            return true;
        }
        else return false;
    }
    public int getMoney() {
        return money;
    }

    public Double AttackPower(){
        Double result=0.0;
        result += weapon.getAttPow();
        return (result + attack) * (0.75 + Math.random()*1.25);
    }
    public Integer GetAttackElem(){
            return  weapon.getElem();
    }
    public Double DeffencePower(){
        Double result = 0.0;
        result += shield.getDefPoint();
        return result +=deffence;
    }
    public Double RestoreHP(){
        Double res = HPrestoreAbility * (0.8 + Math.random()*1.2);
        if (HP+res <=100){
            HP += res;
        }
         else
        {
            res = 100. - HP;
            HP =100.;
        }
        return  res;
    }

    public double GotHarm(Double harm, Integer elem){
        harm = (harm - DeffencePower() ) * ElemInteraction(elem, shield.getElem());
        if(harm>0){
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

    public  playerClass(){
        money = 0;
        attack = 25.;
        deffence = 0.;
        HP = 100.;
        HPrestoreAbility = 10.;
        Shield ShieldIn = new Shield();
        shield = ShieldIn;
        Weapon WeaponIn = new Weapon();
        weapon = WeaponIn;

    }
}



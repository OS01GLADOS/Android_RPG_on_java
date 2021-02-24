package com.belstu.teya.game01.dbHelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.belstu.teya.game01.MainActivity;
import com.belstu.teya.game01.R;
import com.belstu.teya.game01.enemy.enemyClass;
import com.belstu.teya.game01.player.equipment.Equpment;
import com.belstu.teya.game01.player.equipment.Shield;
import com.belstu.teya.game01.player.equipment.Weapon;

import java.io.ByteArrayOutputStream;

public class dbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GAME.db";

    Context contextDB;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contextDB = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists MONSTERS(\n" +
                "ID integer  PRIMARY KEY,\n" +
                "NAME text not null,\n" +
                "HP real not null,\n" +
                "DEF real not null,\n" +
                "ATK real not null,\n" +
                "ELEM integer not null,\n" +
                "CD integer not null,\n" +
                "MONEY integer not null,\n" +
                "IMAGE blob\n" +
                ");");
        db.execSQL("\n" +
                "create table if not exists EQUIPMENT(\n" +
                "ID integer PRIMARY KEY,\n" +
                "NAME text not null,\n" +
                "ELEM integer not null, \n" +
                "TYPE text not null, \n" +
                "VALUE real not null, \n" +
                "PRICE integer not null \n" +
                ");");
        AddEquipment(db);
        AddMonsters(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists MONSTERS");
        db.execSQL("drop table if exists EQUIPMENT");
        onCreate(db);
    }
    public String getImageFromResourses(int res){
        byte[] img;
        Bitmap bit;
        bit = BitmapFactory.decodeResource(contextDB.getResources(),res);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 20, stream);
        img = stream.toByteArray();
        String imgStr;
        imgStr = android.util.Base64.encodeToString(img, Base64.DEFAULT);
        return imgStr;
    }

    public void AddMonsters(SQLiteDatabase db){
        //add 4 monsters
        //
        String img1 =  getImageFromResourses(R.drawable.z_simple_slime);
        String img2 = getImageFromResourses(R.drawable.z_fire_slime);
        String img3 = getImageFromResourses(R.drawable.z_water_slime);
        String img4 = getImageFromResourses(R.drawable.z_electro_slime);
        db.execSQL("INSERT into MONSTERS VALUes (1,'Simple slime', 100, 0, 25, 0, 2, 20, '"+img1+"'),\n" +
                "(2,'Fire slime', 90, 0, 30, 1, 1, 30, '"+img2+"'),\n" +
                "(3, 'Water slime', 150, 0, 20, 2, 3, 15, '"+img3+"'),\n" +
                "(4, 'ElectroSlime', 95, 0, 25, 3,1, 25,  '"+img4+"')");
    }
    public void AddEquipment(SQLiteDatabase db){
        //add 8 equip
        db.execSQL("INSERT into EQUIPMENT VALUES (1, 'common sword', 0, 'sword', 15, 50),\n" +
                "(2, 'common shield', 0, 'shield', 10, 60),\n" +
                "(3, 'fire sword', 1, 'sword', 30, 700),\n" +
                "(4, 'fire shield', 1, 'shield', 14, 170),\n" +
                "(5, 'water sword', 2, 'sword', 15, 100),\n" +
                "(6, 'water shield', 2, 'shield', 30, 400),\n" +
                "(7, 'electro sword', 3 , 'sword', 22, 200),\n" +
                "(8, 'elecrto shield', 3, 'shield' , 21, 250)");
    }
    public Equpment GetEquipment (SQLiteDatabase db, int id){
        Cursor cursor;
        cursor = db.rawQuery("SELECT * from EQUIPMENT where id = "+id,null);
        cursor.moveToFirst();
        int IDindex = cursor.getColumnIndex("ID");
        int NAMEindex = cursor.getColumnIndex("NAME");
        int ELEMindex = cursor.getColumnIndex("ELEM");
        int TYPEindex = cursor.getColumnIndex("TYPE");
        int VALUEindex = cursor.getColumnIndex("VALUE");
        int PRICEindex = cursor.getColumnIndex("PRICE");
        Equpment Res;
        Log.d("GetEquipment",cursor.getString(TYPEindex));
        String type = cursor.getString(TYPEindex);
        if ( type.equals("shield"))
        {
            Res = new Shield(cursor.getInt(IDindex),
                    cursor.getInt(ELEMindex),
                    cursor.getString(NAMEindex),
                    cursor.getDouble(VALUEindex),
                    cursor.getInt(PRICEindex)
            );
        }
        else
        {
            Res = new Weapon(cursor.getInt(IDindex),
                    cursor.getInt(ELEMindex),
                    cursor.getString(NAMEindex),
                    cursor.getDouble(VALUEindex),
                    cursor.getInt(PRICEindex)
            );
        }

        return Res;
    }
    public Integer GetEquipmentCount(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*)[res] from EQUIPMENT", null);
        cursor.moveToFirst();
        int resIndex = cursor.getColumnIndex("res");
        Log.d("EquipmentCount",cursor.getString(resIndex));
        return cursor.getInt(resIndex);
    }
    public enemyClass GetMonster (SQLiteDatabase db){
        int id = 1 + (int)(Math.random()* GetMonstersCount(db)) ;
        Cursor cursor;
        cursor = db.rawQuery("select * from Monsters where id = "+id,null);
        cursor.moveToFirst();
        int IDindex = cursor.getColumnIndex("ID");
        int NAMEindex = cursor.getColumnIndex("NAME");
        int HPindex = cursor.getColumnIndex("HP");
        int DEFindex = cursor.getColumnIndex("DEF");
        int ATKindex = cursor.getColumnIndex("ATK");
        int ELEMindex = cursor.getColumnIndex("ELEM");
        int CDindex = cursor.getColumnIndex("CD");
        int MONEYindex = cursor.getColumnIndex("MONEY");
        int IMAGEindex = cursor.getColumnIndex("IMAGE");
        cursor.moveToFirst();
        return new enemyClass(
                cursor.getInt(IDindex),
                cursor.getString(NAMEindex),
                cursor.getInt(MONEYindex),
                cursor.getDouble(HPindex),
                cursor.getDouble(DEFindex),
                cursor.getDouble(ATKindex),
                cursor.getInt(ELEMindex),
                cursor.getInt(CDindex),
                cursor.getString(IMAGEindex)
                );

    }
    public Integer GetMonstersCount(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*)[res] from MONSTERS", null);
        cursor.moveToFirst();
        int resIndex = cursor.getColumnIndex("res");
        Log.d("MonsterCount",cursor.getString(resIndex));
        return cursor.getInt(resIndex);
    }
}

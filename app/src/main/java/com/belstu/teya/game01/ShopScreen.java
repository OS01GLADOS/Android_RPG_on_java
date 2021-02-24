package com.belstu.teya.game01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.belstu.teya.game01.BGMservice.BGMservice;
import com.belstu.teya.game01.MainActivity;
import com.belstu.teya.game01.R;
import com.belstu.teya.game01.ShopScreenCustomAdapter.CustomAdapter;
import com.belstu.teya.game01.battle.BattleScreen;
import com.belstu.teya.game01.dbHelper.dbHelper;
import com.belstu.teya.game01.player.equipment.Equpment;
import com.belstu.teya.game01.player.equipment.Shield;
import com.belstu.teya.game01.player.equipment.Weapon;
import com.belstu.teya.game01.player.playerClass;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ShopScreen extends AppCompatActivity {

    Intent BGM;

    public static CustomAdapter adapter;
    public ArrayList<Equpment> DataSet;
    playerClass Player;
    public ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_screen);
        Intent intent = getIntent();
        Player = (playerClass)intent.getSerializableExtra("Player");
        setMoney(Player.getMoney());
        setPlayerInventory(Player);

        listView = findViewById(R.id.shopItems);

        DataSet = getShopItems();
        adapter = new CustomAdapter(DataSet,getApplicationContext());

        listView.setAdapter(adapter);


        registerForContextMenu(listView);
        BGM = new Intent(this, BGMservice.class);
        startService(BGM);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(BGM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(BGM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(BGM);

    }

    public  void setPlayerInventory(playerClass Player){
        String WName = Player.getWeapon().getName();
        String SName = Player.getShield().getName();
        TextView PlayerEqTextView = findViewById(R.id.shopPlayerEquip);
        PlayerEqTextView.setText("Your equipment:\nWeapon: "+WName+"\nShield: "+SName);
    }
    public void setMoney(int Money){
        TextView money = findViewById(R.id.shopMoney);
        money.setText("Money : "+Money+" g");
    }

    public void savePlayer(){
        File f = new File(super.getFilesDir(), "save.json");
        Gson gson = new Gson();
        String jb = gson.toJson(Player);
        FileWriter fileWriter;
        if (f.exists())
        {
            Log.d("Save Player", "Файл save.json существует. Его путь " + f.getAbsolutePath());
            f.delete();
        }
        try{
            f.createNewFile();
            Log.d("Save Player", "Файл save.json создан");
        }
        catch (IOException e)
        {
            Log.e("Save Player","Файл save.json не создан");
        }
        try {
            fileWriter = new FileWriter(f);
            fileWriter.write(jb);
            Log.d("Save Player", "Data recorded");
            fileWriter.close();
        }
        catch (IOException e) {
            Log.e("Save Player", e.getMessage());
            e.printStackTrace();
        }
    }
    public void onStartClick(View view){
        savePlayer();
        Intent intent = new Intent(this, BattleScreen.class);
        intent.putExtra("Player",(Serializable)Player);
        startActivity(intent);
        stopService(BGM);
    }
    public void onSaveAndExitClick(View view){
        savePlayer();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("BGM",false);
        startActivity(intent);
    }

    public ArrayList<Equpment> getShopItems(){
        dbHelper helper = new dbHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        Integer num1 , num2, num3 = 3;
        int elemCount = helper.GetEquipmentCount(database);

        num1 = getRandomInD(1,elemCount);
        Log.d("Number1",num1.toString());

        do{
            num2=getRandomInD(1,elemCount);
        }while (num1 == num2);
        Log.d("Number2",num2.toString());
        do{
            num3=getRandomInD(1,elemCount);
        }while (num3 == num2 || num3 == num1);
        Log.d("Number3",num3.toString());
        ArrayList<Equpment> Res = new ArrayList<Equpment>();

        Equpment Elem1 = helper.GetEquipment(database,num1);
        Equpment Elem2 = helper.GetEquipment(database,num2);
        Equpment Elem3 = helper.GetEquipment(database,num3);
        Res.add(Elem1);
        Res.add(Elem2);
        Res.add(Elem3);
        return Res;
    }
    public int getRandomInD(int start, int end){
        return start + (int)(Math.random() * end);
    }
    public Equpment SelectedShopElem;
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.elem_context_menu,menu);
        ListView lv = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        SelectedShopElem = (Equpment)lv.getItemAtPosition(acmi.position);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.ShopBuy:
            {
                Log.d("Shop","ShopBuy");
                if(Player.SpendMoney(SelectedShopElem.getPrice())){
                    if(SelectedShopElem.getType1() == "shield") {
                        Player.setShield((Shield)SelectedShopElem);
                    }
                    else {
                        Player.setWeapon((Weapon)SelectedShopElem);
                    }
                    //toast
                    Toast.makeText(this, "Item bought", Toast.LENGTH_LONG).show();
                    setPlayerInventory(Player);
                    setMoney(Player.getMoney());
                }
                else {
                    //toast
                    Toast.makeText(this, "Not enough Gold", Toast.LENGTH_LONG).show();
                }
            }
                break;
            case R.id.ShopCompare:
            {
                Log.d("Shop","ShopCompare");
                showDialogs(R.layout.dialog_compare);
            }
                break;
        }
        return false;
    }
    AlertDialog alertDialog;
    public void showDialogs(int res){
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(res, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
        TextView PItemName, PItemVal, PItemElem,
                CItemName, CItemVal, CItemElem, Val;
        Equpment PElem;


        PItemElem = dialogView.findViewById(R.id.PlayerItemElem);
        PItemName = dialogView.findViewById(R.id.PlayerItemName);
        PItemVal = dialogView.findViewById(R.id.PlayerItemValue);

        Val = dialogView.findViewById(R.id.Label3);

        CItemElem = dialogView.findViewById(R.id.CompItemElem);
        CItemName = dialogView.findViewById(R.id.CompItemName);
        CItemVal = dialogView.findViewById(R.id.CompItemValue);

        if(SelectedShopElem.getType1() == "shield")
        {
            Val.setText("DEFENCE");
            PElem = Player.getShield();
            PItemVal.setText(Player.getShield().getDefPoint()+"");
            Shield CItem =(Shield) SelectedShopElem;
            CItemVal.setText(CItem.getDefPoint()+"");
        }
        else {
            Val.setText("ATTACK");
            PElem = Player.getWeapon();
            PItemVal.setText(Player.getWeapon().getAttPow()+"");
            Weapon CItem =(Weapon) SelectedShopElem;
            CItemVal.setText(CItem.getAttPow()+"");
        }

        PItemElem.setText(GetElem(PElem.getElem()));
        CItemElem.setText(GetElem(SelectedShopElem.getElem()));

        PItemName.setText(PElem.getName());
        CItemName.setText(SelectedShopElem.getName());

        mDialogBuilder.setView(dialogView);
        mDialogBuilder
                .setTitle("Compare")
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialog = mDialogBuilder.create();
        alertDialog.show();

    }
    public String GetElem(Integer elem){
        switch (elem){
            case 0://
                return "none";
            case 1:
                return  "fire";
            case 2:
                return "water";
            case 3:
                return  "electricity";
            default:
                return null;
        }
    }
}
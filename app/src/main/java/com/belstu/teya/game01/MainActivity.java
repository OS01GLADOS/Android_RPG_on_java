package com.belstu.teya.game01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.belstu.teya.game01.BGMservice.BGMservice;
import com.belstu.teya.game01.player.playerClass;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer player;
    playerClass Player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image;
        image = findViewById(R.id.LOGOmain);
        image.setImageResource(R.drawable.ev12_main);
        Button loadBtn = findViewById(R.id.loadGame);
        loadBtn.setOnClickListener(this);
        File f = new File(super.getFilesDir(), "save.json");
        if (!f.exists()){
            loadBtn.setEnabled(false);
        }
        Button StartBtn = findViewById(R.id.startGame);
        StartBtn.setOnClickListener(this);
        player = MediaPlayer.create(this, R.raw.menu_theme);
        player.setLooping(true);
        player.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loadGame:
                Gson gson = new Gson();
                File f = new File(getFilesDir(), "save.json");
                try{
                    FileReader fileReader = new FileReader(f);
                    char[] buf = new char[(int)f.length()];
                    fileReader.read(buf);
                    fileReader.close();
                    String s = new String(buf);
                    Player= gson.fromJson(s,playerClass.class);
                    Log.d("Save Player", "File read from json");

                } catch (IOException e) {
                    Log.d("Save Player", "readFromJson: "+ e.getMessage());
                    Player = new playerClass();
                }
                break;
            case R.id.startGame:
                Player = new playerClass();
                break;
        }
        Intent intent = new Intent(this, ShopScreen.class);
        intent.putExtra("Player",(Serializable)Player);
        startActivity(intent);
    }



}
package com.belstu.teya.game01.battle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.belstu.teya.game01.R;
import com.belstu.teya.game01.ShopScreen;
import com.belstu.teya.game01.player.playerClass;

import java.io.Serializable;

public class BattleWinScreen extends AppCompatActivity implements View.OnClickListener {



    MediaPlayer player;
    playerClass Player;
    int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_win_screen);
        Intent intent = getIntent();
        Player = (playerClass)intent.getSerializableExtra("Player");
        money = intent.getIntExtra("EarnMoney", 0);
        Player.AddMoney(money);
        TextView earn = findViewById(R.id.earn);
        earn.setText("Earned: "+money+" gold.");
        ConstraintLayout layout = findViewById(R.id.battleWinScreen);
        layout.setOnClickListener(this);


        player = MediaPlayer.create(this, R.raw.battle_win);
        player.setLooping(false);
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
        Intent intent = new Intent(this, ShopScreen.class);
        intent.putExtra("Player",(Serializable)Player);
        startActivity(intent);
    }
}
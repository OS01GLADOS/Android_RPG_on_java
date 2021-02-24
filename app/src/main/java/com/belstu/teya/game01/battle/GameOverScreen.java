package com.belstu.teya.game01.battle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.belstu.teya.game01.MainActivity;
import com.belstu.teya.game01.R;

public class GameOverScreen extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        Intent intent = getIntent();
        String enemyName = intent.getStringExtra("EnemyName");
        TextView earn = findViewById(R.id.earn);
        earn.setText("you were slained by \n"+enemyName);
        ConstraintLayout layout = findViewById(R.id.battleGameOverScreen);
        layout.setOnClickListener(this);
        player = MediaPlayer.create(this, R.raw.game_over);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
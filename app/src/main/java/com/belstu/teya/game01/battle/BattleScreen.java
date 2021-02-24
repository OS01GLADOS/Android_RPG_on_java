package com.belstu.teya.game01.battle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.belstu.teya.game01.R;
import com.belstu.teya.game01.dbHelper.dbHelper;
import com.belstu.teya.game01.enemy.enemyClass;
import com.belstu.teya.game01.player.playerClass;

import java.io.Serializable;

public class BattleScreen extends AppCompatActivity implements View.OnClickListener {

    Context context;

    MediaPlayer playerBGM;
    MediaPlayer playerHeroHealEff;
    MediaPlayer playerEnemyAtk;
    MediaPlayer playerHeroAtk;
    MediaPlayer playerGetHit;

    playerClass Player;
    enemyClass Enemy;

    int cooldown;
    int currCooldown;

    TextView EnemyStats;
    TextView PlayerStats;
    TextView BattleLog;

    ImageView EnemyIcon;
    ImageView EnemyParticle;//частицы у игрока при атаке монстра

    ImageView HeroIcon;
    ImageView HeroParticle;//частицы у монстра при атаке игрока
    ImageView HeroHealParticle;//частицы у игрока при лечении

    ConstraintLayout layout;
    ConstraintLayout HeroFull;
    ConstraintLayout EnemyFull;

    SQLiteDatabase database;
    dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_screen);

        context = this;

        EnemyStats = findViewById(R.id.enemyStats);
        PlayerStats = findViewById(R.id.playerStats);

        Button attackBtn = findViewById(R.id.attack);
        attackBtn.setOnClickListener(this);
        Button healBtn = findViewById(R.id.heal);
        healBtn.setOnClickListener(this);
        Button runBtn = findViewById(R.id.run);
        runBtn.setOnClickListener(this);

        Intent intent = getIntent();
        Player = (playerClass)intent.getSerializableExtra("Player");

        dbHelper = new dbHelper(this);
        database = dbHelper.getWritableDatabase();
        Enemy = dbHelper.GetMonster(database);

        EnemyIcon = findViewById(R.id.enemyIcon);
        EnemyParticle= findViewById(R.id.battleHeroHitParticle);

        HeroIcon = findViewById(R.id.battleHeroIcon);
        HeroParticle = findViewById(R.id.enemyHitParticle);
        HeroHealParticle = findViewById(R.id.battleHeroHealParticle);

        layout = findViewById(R.id.VisualImg);
        layout.setBackgroundResource(R.drawable.battle_bgr);

        byte[] image = Base64.decode(Enemy.ByteImage, Base64.DEFAULT);
        Bitmap bmp = null;
        if(image!=null && image.length>0)
        {
            bmp = BitmapFactory.decodeByteArray(image,0,image.length);
        }
        EnemyIcon.setImageBitmap(bmp);
        HeroIcon.setImageResource(R.drawable.knight1);

        dbHelper.GetEquipmentCount(database);

        cooldown = Enemy.getMinCooldown();
        currCooldown = cooldown;
        SetText();
        SetLogText(Enemy.getName()+" appears!");
        SetLogText("Let The Battle Begin!");

        playerBGM = MediaPlayer.create(this, R.raw.battle_theme);
        playerBGM.setLooping(true);
        playerBGM.start();
        playerHeroHealEff = MediaPlayer.create(this, R.raw.heal);
        playerHeroHealEff.setLooping(false);
        playerGetHit = MediaPlayer.create(this,R.raw.hit);
        playerGetHit.setLooping(false);

        switch (Enemy.getElem())
        {
            case 1:
                playerEnemyAtk = MediaPlayer.create(this,R.raw.fire);
                EnemyParticle.setImageResource(R.drawable.e_fire);
                break;
            case 2:
                playerEnemyAtk = MediaPlayer.create(this,R.raw.water);
                EnemyParticle.setImageResource(R.drawable.e_water);
                break;
            case 3:
                playerEnemyAtk = MediaPlayer.create(this,R.raw.electricity);
                EnemyParticle.setImageResource(R.drawable.e_elec);
                break;
            default:
                playerEnemyAtk = MediaPlayer.create(this,R.raw.punch);
        }
        playerEnemyAtk.setLooping(false);

        switch (Player.getWeapon().getElem())
        {
            case 1:
                playerHeroAtk = MediaPlayer.create(this,R.raw.fire);
                HeroParticle.setImageResource(R.drawable.e_fire);
                break;
            case 2:
                playerHeroAtk = MediaPlayer.create(this,R.raw.water);
                HeroParticle.setImageResource(R.drawable.e_water);
                break;
            case 3:
                playerHeroAtk = MediaPlayer.create(this,R.raw.electricity);
                HeroParticle.setImageResource(R.drawable.e_elec);
                break;
            default:
                playerHeroAtk = MediaPlayer.create(this,R.raw.punch);
        }
        playerHeroAtk.setLooping(false);
        HeroHealParticle.setImageResource(R.drawable.e_heal);

        HeroParticle.setVisibility(View.INVISIBLE);
        HeroHealParticle.setVisibility(View.INVISIBLE);
        EnemyParticle.setVisibility(View.INVISIBLE);

        HeroFull = findViewById(R.id.battleHeroElem);
        EnemyFull = findViewById(R.id.EnemyElem);

    }

    void SetText(){
        PlayerStats.setText(Player.toString());
        EnemyStats.setText(Enemy.toString());
    }
    void SetLogText(String text){
        BattleLog = findViewById(R.id.battleLog);
        BattleLog.setText(BattleLog.getText().toString() + "\n"+text);
    }
    void SetLogText(){

        BattleLog = findViewById(R.id.battleLog);
        BattleLog.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerBGM.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
       playerBGM.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerBGM.stop();
    }
    @Override
    public void onClick(View v) {
        Button button;
        button =findViewById(R.id.attack);
        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.heal);
        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.run);
        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        SetLogText();
        switch (v.getId()) {
            case R.id.attack: {
                Animation animation = AnimationUtils.loadAnimation(context,R.anim.hero_att_anim1);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        SetLogText("Player attacks!");
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation animation2 = AnimationUtils.loadAnimation(context,R.anim.hero_att_anim2);
                        animation2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                HeroIcon.setImageResource(R.drawable.knight2);
                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                HeroIcon.setImageResource(R.drawable.knight3);
                                SetLogText(Enemy.getName() + " get " + Enemy.GotHarm(Player.AttackPower(), Player.GetAttackElem()) + " damage!");
                                SetText();
                                Animation animation3 = AnimationUtils.loadAnimation(context,R.anim.hero_att_anim3);
                                animation3.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        HeroParticle.setVisibility(View.VISIBLE);
                                        playerHeroAtk.start();
                                    }
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        HeroIcon.setImageResource(R.drawable.knight1);
                                        HeroParticle.setVisibility(View.INVISIBLE);
                                        Animation animation4 = AnimationUtils.loadAnimation(context, R.anim.hero_att_anim4);
                                        animation4.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                            }
                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                monsterAttack();
                                            }
                                            @Override
                                            public void onAnimationRepeat(Animation animation) {
                                            }
                                        });
                                        HeroFull.startAnimation(animation4);
                                    }
                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                                EnemyIcon.startAnimation(animation3);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        HeroIcon.startAnimation(animation2);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                HeroFull.startAnimation(animation);
            }
            break;
            case R.id.heal: {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.heal_anim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        SetLogText("Player restores " + Player.RestoreHP() + " HP");
                        SetText();
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        monsterAttack();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                playerHeroHealEff.start();
                HeroHealParticle.startAnimation(animation);

            }
            break;
            default:
                SetLogText("Player waits...");
                monsterAttack();
                break;
        }
    }
    private void monsterAttack() {
        if (currCooldown ==0) {
            SetLogText(Enemy.getName() + " Attacks!");
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.monster_att_anim1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation animation2 = AnimationUtils.loadAnimation(context,R.anim.hero_att_anim3);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            EnemyParticle.setVisibility(View.VISIBLE);
                            playerEnemyAtk.start();
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            EnemyParticle.setVisibility(View.INVISIBLE);
                            SetLogText("Player get " + Player.GotHarm(Enemy.Attack(), Enemy.getElem()) + " damage!");
                            Animation animation3 = AnimationUtils.loadAnimation(context,R.anim.hero_att_anim3);
                            animation3.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    playerGetHit.start();
                                }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    currCooldown = 1 + (int) Math.random() * cooldown;
                                    Animation animation4 = AnimationUtils.loadAnimation(context,R.anim.monster_att_anim2);
                                    animation4.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {}

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            SetText();
                                            Button button;
                                            button =findViewById(R.id.attack);
                                            button.setEnabled(true);
                                            button.setVisibility(View.VISIBLE);
                                            button = findViewById(R.id.heal);
                                            button.setEnabled(true);
                                            button.setVisibility(View.VISIBLE);
                                            button = findViewById(R.id.run);
                                            button.setEnabled(true);
                                            button.setVisibility(View.VISIBLE);
                                            EndTurnActions();
                                        }
                                        @Override
                                        public void onAnimationRepeat(Animation animation) {}
                                    });
                                    EnemyFull.startAnimation(animation4);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                            HeroIcon.startAnimation(animation3);

                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    EnemyParticle.startAnimation(animation2);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            EnemyFull.startAnimation(animation);
        }
        else
        {
            currCooldown --;
            Button button;
            button =findViewById(R.id.attack);
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
            button = findViewById(R.id.heal);
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
            button = findViewById(R.id.run);
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
            EndTurnActions();
        }

    }
    private void EndTurnActions() {
        if(Player.IsDead()) {
            Intent intent = new Intent(this, GameOverScreen.class);
            intent.putExtra("EnemyName",Enemy.getName());
            startActivity(intent);
            playerBGM.stop();
        }
        //game over Screen
        if(Enemy.IsDead()) {
            //win game Screen
            Intent intent = new Intent(this, BattleWinScreen.class);
            intent.putExtra("Player",(Serializable)Player);
            intent.putExtra("EarnMoney", Enemy.getMoney());
            startActivity(intent);
            playerBGM.stop();
        }

    }
}




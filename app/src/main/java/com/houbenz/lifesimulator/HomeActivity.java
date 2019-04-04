package com.houbenz.lifesimulator;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import androidx.lifecycle.ViewModelProviders;
import beans.Level;
import conf.Params;
import database.Player;
import fragments.HomeFragment;
import fragments.RelationFragment;
import viewmodels.ViewModelPartner;


public class HomeActivity extends AppCompatActivity {


    private Player player;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private ConstraintLayout mConstraintLayout;
    private int slot ;
    private TextView dayView;
    private TextView time;

    private Button showHomeButton;
    private Button socialButton;


    private ProgressBar healthbar;
    private ProgressBar energyBar;
    private ProgressBar hungerBar;

    private SeekBar speedSeekBar;

    private TextView healthpr;
    private TextView energypr;
    private TextView hungerpr;
    private TextView balance;
    private TextView levelNumber;

    private TextView speedName;

    boolean threadRun=true;

    private int day;
    private int hour;
    private int minute;
    private int totalTime;

    private ViewModelPartner viewModel;
    private int speed=Params.TIME_SPEED_NORMAL;
    private View.OnTouchListener mOnTouchListener = (v, event) ->{

        hideSystemUI();
        return false;
    } ;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }


    private void hideSystemUI() {

        View decorView = getWindow().getDecorView();


        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }

    @Override
    public void onBackPressed() {

        saveProgress();
        Intent intent = new Intent(this,GameScene.class);
        intent.putExtra("slotNumber",player.getId());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        saveProgress();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        saveProgress();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dayView=findViewById(R.id.dayView);
        time=findViewById(R.id.time);
        healthbar=findViewById(R.id.healthBar);
        energyBar=findViewById(R.id.energyBar);
        hungerBar=findViewById(R.id.hungerBar);
        healthpr=findViewById(R.id.healthpr);
        hungerpr=findViewById(R.id.hungerpr);
        energypr=findViewById(R.id.energypr);
        balance=findViewById(R.id.balance);
        levelNumber=findViewById(R.id.levelNumber);
        speedSeekBar=findViewById(R.id.speedSeekBar);
        speedName=findViewById(R.id.speedName);


        mConstraintLayout=findViewById(R.id.constraintLayout);
        mConstraintLayout.setOnTouchListener(mOnTouchListener);

        slot =getIntent().getIntExtra("slot",0);
        loadProgress();

        HomeFragment homeFragment1 = new HomeFragment();
        insertFragment(homeFragment1);


        showHomeButton=findViewById(R.id.showHomeButton);

        showHomeButton.setOnClickListener(view ->{
            HomeFragment homeFragment = new HomeFragment();
            insertFragment(homeFragment);
        });


        socialButton=findViewById(R.id.socialButton);
        socialButton.setOnClickListener(view ->{
            RelationFragment relationFragment = new RelationFragment();
            insertFragment(relationFragment);
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if(progress==0){
                    speedName.setText(getString(R.string.normalSpeed));
                    speed=Params.TIME_SPEED_NORMAL;
                };
                if(progress==1){
                    speedName.setText(getString(R.string.fastSpeed));
                    speed=Params.TIME_SPEED_FAST;
                };
                if(progress==2){
                    speedName.setText(getString(R.string.ultraSpeed));
                    speed=Params.TIME_SPEED_ULTRA_FAST;
                };
                if(progress==3){
                    speedName.setText(getString(R.string.superSpeed));
                    speed=Params.TIME_SPEED_SUPER_FAST;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        loadProgress();
        runClockThread();
        initialiseProgressBars();


        viewModel = ViewModelProviders.of(this).get(ViewModelPartner.class);


        viewModel.isFoundPartner().observe(this,isFound ->{
            player.setDating("true");

        });


        viewModel.isBreakUp().observe(this,isbreakUp ->{
            player.setDating("false");
        });

    }

    public void insertFragment(Fragment fragment){

        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();

        Bundle bundle = new Bundle();
        bundle.putInt("slot",slot);
        fragment.setArguments(bundle);
        transaction =fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        transaction.replace(R.id.placeFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void runClockThread(){

        Thread thread = new Thread(() -> {

            while (threadRun) {
                try {

                    Thread.sleep(speed);
                    runOnUiThread(() -> {

                        //For GAME OVER
                        if (healthbar.getProgress() <= 0) {
                            threadRun = false;
                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_toast_red);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView message = dialog.findViewById(R.id.message);

                            message.setText("Game Over ! :( your health is Zero");
                            dialog.setOnDismissListener(dialog1 -> {
                                dialog1.dismiss();
                                finish();
                            });

                            MainMenu.myAppDataBase.myDao().deletePlayer(player);
                            dialog.show();
                        }

                        String hourS = "" + hour;
                        String minuteS = "" + minute;
                        String dayS = "" + day;

                        totalTime = (day * 24 * 60) + (hour * 60) + minute;

                        if (hour < 10)
                            hourS = "0" + hour;

                        if (minute < 10)
                            minuteS = "0" + minute;

                        if (day < 10)
                            dayS = "0" + day;

                        minute++;

                        if (minute == 60) {
                            minute = 0;
                            hour++;
                            hungerBar.setProgress(hungerBar.getProgress() - Params.HUNGER_LOSS_PER_HOUR);
                            hungerpr.setText(hungerBar.getProgress() + "/" + hungerBar.getMax());

                            if (hungerBar.getProgress() == 0 && energyBar.getProgress() == 0) {

                                healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());

                            } else {
                                if (hungerBar.getProgress() == 0 && energyBar.getProgress() > 0) {

                                    healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR);
                                    healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
                                } else {
                                    if (hungerBar.getProgress() > 0 && energyBar.getProgress() == 0) {
                                        healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                        healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
                                    }
                                }
                            }

                        }
                        if (hour >= 24) {
                            hour = 0;
                            minute = 0;
                            day++;

                            player.setBank_deposit(player.getBank_deposit() * 1.01f);
                            if (player.getStore_income() != 0) {

                                player.setBalance(player.getBalance() + player.getStore_income());
                                balance.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(() -> {
                                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.money_gain);
                                    mp.start();
                                    balance.setText(player.getBalance() + "$");
                                    balance.animate().scaleX(1f).scaleY(1f).setDuration(300);
                                });
                            }
                            //saveProgress();
                        }
                        time.setText(hourS + ":" + minuteS);
                        dayView.setText(getString(R.string.day) + " " + dayS);

                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!threadRun)
                    break;

            }
        });

            thread.start();
    }



    public void loadProgress(){
        database.Player loaded_player = MainMenu.myAppDataBase.myDao().getPlayer(slot);
        //jobName.setText(loaded_player.getWork());
       // caracterImg.setImageURI(Uri.parse(loaded_player.getWork_image_path()));
        balance.setText(loaded_player.getBalance()+"$");
        //income.setText((int)loaded_player.getWork_income());
        levelNumber.setText("lvl"+loaded_player.getLevel());
        hour=loaded_player.getHour();
        minute=loaded_player.getMinute();
        day=loaded_player.getDay();

        totalTime= day * 24 + hour * 60 + minute;

        player=loaded_player;


        Level level = new Level(player.getLevel(),player.getLevel_progress(),player.getMax_progress());
        player.setLevel_object(level);


        healthbar.setProgress(loaded_player.getHealthbar());
        energyBar.setProgress(loaded_player.getEnergybar());
        hungerBar.setProgress(loaded_player.getHungerbar());


    }

    public void saveProgress(){

        player.setHour(hour);
        player.setMinute(minute);
        player.setDay(day);

        player.setHealthbar(healthbar.getProgress());
        player.setHungerbar(hungerBar.getProgress());
        player.setEnergybar(energyBar.getProgress());

        player.setLevel(player.getLevel_object().getLevel());
        player.setLevel_progress(player.getLevel_object().getProgressLevel());
        player.setMax_progress(player.getLevel_object().getMaxProgress());

        double workincome = MainMenu.myAppDataBase.myDao().work_incorme(player.getWork());

        player.setWork_income(workincome);

        MainMenu.myAppDataBase.myDao().updatePlayer(player);


        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Games.getLeaderboardsClient(this, account).submitScore(getString(R.string.leaderboard_score),
                    (long) player.getBalance());

        }

    }

    public void initialiseProgressBars(){

        healthbar.setIndeterminate(false);
        healthbar.setMax(100);

        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());

        energyBar.setIndeterminate(false);
        energyBar.setMax(100);

        energypr.setText(energyBar.getProgress()+"/"+energyBar.getMax());

        hungerBar.setIndeterminate(false);
        hungerBar.setMax(100);

        hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());


        if(player.getLevel_object().getLevel()<10)
            levelNumber.setText(getString(R.string.level)+": 0"+player.getLevel_object().getLevel());

        else
            levelNumber.setText(getString(R.string.level)+": "+player.getLevel_object().getLevel());

    };
}

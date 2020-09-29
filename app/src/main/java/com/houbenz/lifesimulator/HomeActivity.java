package com.houbenz.lifesimulator;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;

import beans.Level;
import conf.Params;
import database.Player;
import fragments.Home2Fragment;
import fragments.HomeFragment;
import fragments.OutsideHome2Fragment;
import fragments.OutsideHomeFragment;
import fragments.RelationFragment;
import viewmodels.ViewModelPartner;

import static com.houbenz.lifesimulator.MainMenu.MY_PERMISSIONS_REQUEST_EXTERNAL_STROGAE;


public class HomeActivity extends AppCompatActivity {


    private Player player;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private ConstraintLayout mConstraintLayout;
    private int slot ;
    private TextView dayView;
    private TextView time;

    private Button showHomeButton;
    private Button showSecondHomeButton;
    private Button socialButton;
    private Button showOutsideHomeButton;
    private Button showOutsideHome2Button;


    private ProgressBar healthbar;
    private ProgressBar energyBar;
    private ProgressBar hungerBar;

    private SeekBar speedSeekBar;

    private TextView healthpr;
    private TextView energypr;
    private TextView hungerpr;
    private TextView balance;
    private TextView levelNumber;

    private TextView not_owned_text;

    private TextView speedName;

    boolean threadRun=true;

    private int day;
    private int hour;
    private int minute;
    private int totalTime;

    private int minusRelation ;

    private TextView datingMessage;
    private ViewSwitcher switcher;
    private ViewModelPartner viewModel;
    private int speed=Params.TIME_SPEED_NORMAL;
    private View.OnTouchListener mOnTouchListener = (v, event) ->{

        hideSystemUI();
        return false;
    } ;


    private GoogleSignInAccount account;



    public void deselectButtons(){
        showHomeButton.setSelected(false);
        showSecondHomeButton.setSelected(false);
        socialButton.setSelected(false);
        showOutsideHomeButton.setSelected(false);
        showOutsideHome2Button.setSelected(false);
    }

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

        viewModel.getRelationBar().observe(this,relationBar ->{
            player.setRelationBar(relationBar);
        });
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

        viewModel.getRelationBar().observe(this,relationBar ->{
            player.setRelationBar(relationBar);
        });
        saveProgress();
        super.onDestroy();
        threadRun=false;
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
        switcher=findViewById(R.id.switcherHomeActivity);
        showHomeButton=findViewById(R.id.showHomeButton);
        showSecondHomeButton=findViewById(R.id.showSecondHomeButton);
        socialButton=findViewById(R.id.socialButton);
        showOutsideHomeButton=findViewById(R.id.showOutsideHomeButton);
        showOutsideHome2Button=findViewById(R.id.showOutsideHome2Button);
        not_owned_text=findViewById(R.id.not_owned_textview);




        //asks for permission
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                //todo

            }else{

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STROGAE);

            }
        }

        mConstraintLayout=findViewById(R.id.constraintLayout);
        mConstraintLayout.setOnTouchListener(mOnTouchListener);

        slot =getIntent().getIntExtra("slot",0);
        minusRelation=getIntent().getIntExtra("minusRelation",0);
        loadProgress();


        HomeFragment homeFragment1 = new HomeFragment();
        insertFragment(homeFragment1);

        showHomeButton.setSelected(true);

        showHomeButton.setOnClickListener(view ->{

            deselectButtons();
            showHomeButton.setSelected(true);

            HomeFragment homeFragment = new HomeFragment();
            insertFragment(homeFragment);
        });

        socialButton.setOnClickListener(view ->{

            deselectButtons();
            socialButton.setSelected(true);

            RelationFragment relationFragment = new RelationFragment();
            insertFragment(relationFragment);
        });


        showOutsideHomeButton.setOnClickListener( view ->{

            deselectButtons();
            showOutsideHomeButton.setSelected(true);

            OutsideHomeFragment outsideHomeFragment = new OutsideHomeFragment();
            insertFragment(outsideHomeFragment);
        });

        showOutsideHome2Button.setOnClickListener( view ->{

            deselectButtons();
            showOutsideHome2Button.setSelected(true);

            OutsideHome2Fragment outsideHome2Fragment = new OutsideHome2Fragment();
            insertFragment(outsideHome2Fragment);
        });


        showSecondHomeButton.setOnClickListener(view ->{
            deselectButtons();
            showSecondHomeButton.setSelected(true);

            Home2Fragment home2Fragment = new Home2Fragment();
            insertFragment(home2Fragment);
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

        runClockThread();
        initialiseProgressBars();

        viewModel = ViewModelProviders.of(this).get(ViewModelPartner.class);

        viewModel.isFoundPartner().observe(this,isFound ->{
            player.setDating("true");

        });

        viewModel.isBreakUp().observe(this,isbreakUp ->{
            player.setDating("false");
            player.setMarried("false");
        });

        viewModel.getRelationBar().observe(this,relationBar ->{
            player.setRelationBar(relationBar);
        });

        viewModel.isMarried().observe(this,married ->{
            player.setMarried("true");
        });




        //When dating occurs
        viewModel.isGoDate().observe(this,goDate ->{

            if(goDate>0){

                double newBalance =player.getBalance()-goDate;

                if(goDate == 2000)
                    newBalance=player.getBalance();


                    if(newBalance >= 0) {
                    //Show dating animation (means it works)
                    switcher.animate().alpha(0f).setDuration(250).withEndAction(()->{
                       switcher.animate().alpha(1.0f).setDuration(250);
                        switcher.showNext();
                    });
                    animateHearts();
                    player.setBalance(newBalance);
                    balance.setText(player.getBalance() + "$");

                    CountDownTimer timer = new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            //show the previous view (means return to previous  state) with animation
                            switcher.animate().alpha(0f).setDuration(250).withEndAction(()->{
                                switcher.animate().alpha(1.0f).setDuration(250);
                                switcher.showPrevious();
                            });
                        }
                    };

                    timer.start();
                }
            }
        });


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            if (GoogleSignIn.hasPermissions(account)) {

                this.account = account;
                settingUpPopUpforAchievements();
            }
        }
    }

    private void settingUpPopUpforAchievements(){
        GamesClient gamesClient = Games.getGamesClient(this,account);
        gamesClient.setViewForPopups(findViewById(android.R.id.content));
        gamesClient.setGravityForPopups(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    }

    private void animateProgressBar(@NonNull ProgressBar progressBar){
        progressBar.animate().rotation(-5f).scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(()->{

            progressBar.animate().rotation(5f).scaleX(1f).scaleY(1f).setDuration(150).withEndAction(()->{
                progressBar.animate().rotation(0f).setDuration(150);
            }); });
    }
    public void insertFragment(Fragment fragment){

        not_owned_text.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();
        saveProgress();
        Bundle bundle = new Bundle();
        bundle.putInt("slot",slot);
        bundle.putInt("minusRelation",minusRelation);
        fragment.setArguments(bundle);
        transaction =fragmentManager.beginTransaction();
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
                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_toast_red);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView message = dialog.findViewById(R.id.message);

                            message.setText(getString(R.string.zero_health));
                            dialog.setOnDismissListener(dialog1 -> {
                                dialog1.dismiss();
                                finish();
                            });

                            if( ! threadRun) {
                                MainMenu.myAppDataBase.myDao().deletePlayer(player);
                                dialog.show();
                            }
                            threadRun = false;
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



                            if(hungerBar.getProgress() != 0) {
                                animateProgressBar(hungerBar);
                            }

                            if(hungerBar.getProgress() <30 && hungerBar.getProgress() != 0 && healthbar.getProgress() > 30){
                                showCustomToast(getString(R.string.low_hunger),"","yellow");
                            }


                            if (hungerBar.getProgress() == 0 && energyBar.getProgress() == 0) {

                                healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
                                animateProgressBar(healthbar);

                            } else {
                                if (hungerBar.getProgress() == 0 && energyBar.getProgress() > 0) {

                                    healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR);
                                    healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
                                    animateProgressBar(healthbar);
                                } else {
                                    if (hungerBar.getProgress() > 0 && energyBar.getProgress() == 0) {
                                        healthbar.setProgress(healthbar.getProgress() - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                        healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
                                        animateProgressBar(healthbar);
                                    }
                                }
                            }

                            if(healthbar.getProgress() <30 && hungerBar.getProgress() < 30){
                                showCustomToast(getString(R.string.low_health),
                                        "android.resource://com.houbenz.android.lifesimulator/drawable/health","red");
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


    public void showCustomToast(String message , String imgUrl, String type){

        LayoutInflater inflater=getLayoutInflater();
        View layout = null;

        switch (type){
            case "green":
                layout = inflater.inflate(R.layout.custom_toast_green,(ViewGroup)findViewById(R.id.cutom_toast_green));
                break ;
            case "red" :
                layout = inflater.inflate(R.layout.custom_toast_red,(ViewGroup)findViewById(R.id.cutom_toast_red));
                break;
            case "gold" :
                layout=inflater.inflate(R.layout.custom_toast_levelup,(ViewGroup)findViewById(R.id.cutom_toast_levelup));
                break;
            case "yellow" :
                layout=inflater.inflate(R.layout.custom_toast_yellow,(ViewGroup)findViewById(R.id.cutom_toast_yellow));
                break;
            default:layout = inflater.inflate(R.layout.custom_toast_red,(ViewGroup)findViewById(R.id.cutom_toast_red)); ;
        }


        TextView messageTextView = layout.findViewById(R.id.message);
        ImageView imageToast = layout.findViewById(R.id.imageToast);

        imageToast.setVisibility(View.GONE);
        messageTextView.setText(message);

        if (!imgUrl.equals("")) {
            imageToast.setVisibility(View.VISIBLE);

            imageToast.setImageURI(Uri.parse(imgUrl));
        }else {

            if (type.equals("gold"))
                imageToast.setVisibility(View.VISIBLE);
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER ,0,250);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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

        player.setRelationBar(player.getRelationBar() - minusRelation);

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




    public void animateHearts(){

        GridLayout gridLayout =findViewById(R.id.datingScreen);

        LayoutInflater inflater =LayoutInflater.from(getApplicationContext());

        CountDownTimer count =new CountDownTimer(5500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                datingMessage = findViewById(R.id.datingMessage);


                datingMessage.setText(getString(R.string.relation_plus_ten)+ "10");
                datingMessage.setVisibility(View.VISIBLE);
                datingMessage.animate().setDuration(1000).alpha(0f).translationY(-100).withEndAction(() ->{
                    datingMessage.setAlpha(1f);
                    datingMessage.setTranslationY(datingMessage.getTranslationY()+100);
                    datingMessage.setVisibility(View.GONE);
                    gridLayout.removeAllViews();
                });
            }
        };

        count.start();

        for(int i =0 ; i<30;i++){

            View image= inflater.inflate(R.layout.heart_view,null);
            gridLayout.addView(image);

            //animateAlphaZeroToOne(image,2000);
            animateTranslationY(image,6000);
            //animateAlphaOneToZero(image,2000);
        }
    }


    public void animateTranslationY(View image, int duration){

        int rand = (int)(Math.random()*1500) + 1000;
        int finalrand=-( (int)(Math.random()*1000) +500);

        ObjectAnimator animator=ObjectAnimator.ofFloat(image,View.TRANSLATION_Y,rand , finalrand);
        animator.setDuration(duration);
        animator.start();
    }


}

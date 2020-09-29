package com.houbenz.lifesimulator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;

import java.util.List;
import java.util.Locale;

import Workers.SaveToCloudWork;
import beans.Level;
import conf.Params;
import database.Acquired_Cars;
import database.Acquired_Furnitures;
import database.Acquired_Houses;
import database.Acquired_Stores;
import database.Acquired_degree;
import database.Degree;
import database.Food;
import database.Furniture;
import database.House;
import database.Medicine;
import database.Player;
import database.Store;
import database.Work;
import fragments.BankFragment;
import fragments.BuyFragment;
import fragments.CarFragment;
import fragments.DegreeFragment;
import fragments.DepositFragment;
import fragments.FoodFragment;
import fragments.FournitureFragment;
import fragments.GiftFragment;
import fragments.HomeFragment;
import fragments.HouseFragment;
import fragments.PharmacyFragment;
import fragments.SleepFragment;
import fragments.StoreFragment;
import fragments.WithdrawFragment;
import fragments.WorkFragment;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import viewmodels.ViewModelCars;
import viewmodels.ViewModelGift;

import static com.houbenz.lifesimulator.MainMenu.myAppDataBase;


public class GameScene extends AppCompatActivity
        implements WorkFragment.onWorkSelected, BuyFragment.OnMainFragmentClicked,FournitureFragment.OnFournitureClicked
    ,SleepFragment.onSleepClicked,FoodFragment.onFoodClicked, PharmacyFragment.OnMedicineClicked,
        HouseFragment.OnHouseClicked, StoreFragment.OnStoreClicked
    , DegreeFragment.OnDegreeClick, BankFragment.OnDeposit,WithdrawFragment.OnWithdraw,DepositFragment.OnDeposit, RewardedVideoAdListener
{

    private Button buy;
    private Button work;
    private Button sleep;
    private Button bank;
    private Button study;
    private Button homeButton;

    private  boolean  threadRun=true;
    private ProgressBar healthbar;
    private ProgressBar energyBar;
    private ProgressBar hungerBar;

    private ProgressBar levelBar;

    private TextView healthpr;
    private TextView energypr;
    private TextView hungerpr;

    private TextView levelNumber;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;

    private TextView time ;
    private TextView dayView ;
    private TextView playerN;
    private ImageView caracterImg ;
    private TextView jobName;


    private TextView mainText;

    private TextView income;
    private TextView balance;
    private Button  startWorking;
    private Button doubleEarn;

    private  int doubleEarnMinutes ;
    private  int minute ;
    private  int hour ;
    private  int day;
    private  int totalTime;

    private int sleepTime;

    private int speed = Params.TIME_SPEED_NORMAL;

    private SeekBar speedSeekBar;
    private TextView speedName;


    private WorkFragment workFragment ;
    private BankFragment bankFragment;
    private BuyFragment  buyFragment;

    private HomeFragment homeFragment;

    private  Player player;
    private Work choosenWork;

    private ViewSwitcher switcher ;

    //for payment per hour
    private  int remainingMinutes;

    private int slot ;

    private boolean ignore=true;


    private RewardedVideoAd mRewardVideoAdDoubleIncome;

    private ConstraintLayout constraintLayout;

    private ViewModelCars viewModelCars;
    private ViewModelGift viewModelGift;
    MediaPlayer mp;
    MediaPlayer mpIncome;

    private boolean workthreadRun=false ;

    private int minusRelation = 0;

    private Button instantDollarButton;

    private boolean learning =false;
    private int learn_time=Params.LEARN_TIME;



    public final static String appodeal_app_ID ="d759df7a087f4d49bb559c217f05ba70ba91fede02cbfb55";

    //this is the original app ad id
    public final static  String APP_ADS_ID = "ca-app-pub-5859725902066144~3681738021";

     //this is the original video double income ad id
     private final static String AD_VIDEO_ID = "ca-app-pub-5859725902066144/4392184462";


    //this is the original FINDPARTNER AD VIDEO ID
    public final static String AD_VIDEO_PARTNER_ID = "ca-app-pub-5859725902066144/8271930745";


    //this is for test video double ad income ad
    // final static String AD_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";

    //this is the test for FINDPARTNER AD VIDEO ID
   // public final static String AD_VIDEO_PARTNER_ID = "ca-app-pub-3940256099942544/5224354917";



    private GoogleSignInAccount account;



    private View.OnTouchListener mOnTouchListener = (v , event) -> {

            hideSystemUI();
            return false;

    };

    private int duo =0;

    private boolean doubleEarnClicked=false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void runClockThread(){

       @SuppressLint("SetTextI18n") final Thread thread =new Thread(() -> {

            while (threadRun) {

                try {

                    Thread.sleep(speed);
                    runOnUiThread(() -> {

                        //For GAME OVER
                        if(healthbar.getProgress()<=0){

                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_toast_red);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView message= dialog.findViewById(R.id.message);

                            message.setText(getResources().getString(R.string.zero_health));
                            dialog.setOnDismissListener( dialog1 -> {
                                dialog1.dismiss();
                                finish();
                            });


                            if( ! threadRun) {
                                myAppDataBase.myDao().deletePlayer(player);
                                dialog.show();
                            }
                            threadRun=false;
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
                            hungerBar.setProgress(hungerBar.getProgress()-Params.HUNGER_LOSS_PER_HOUR);

                            hungerpr.setText(String.format(Locale.ENGLISH,"%d/%d", hungerBar.getProgress(), hungerBar.getMax()));


                            if(hungerBar.getProgress() != 0) {
                                animateProgressBar(hungerBar);
                            }


                            if(hungerBar.getProgress() <30 && hungerBar.getProgress() != 0 && healthbar.getProgress() > 30){
                                showCustomToast(getResources().getString(R.string.low_hunger),"","yellow");
                            }

                            if(hungerBar.getProgress()==0 && energyBar.getProgress()==0){
                                healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
                                animateProgressBar(healthbar);

                            }else {
                                if(hungerBar.getProgress()==0 && energyBar.getProgress()>0){

                                    healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR );
                                    healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
                                    animateProgressBar(healthbar);
                                }else{
                                    if(hungerBar.getProgress()>0 && energyBar.getProgress()==0){
                                        healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
                                        animateProgressBar(healthbar);
                                    }
                                }

                            }

                            if(healthbar.getProgress() <30 && hungerBar.getProgress() < 30){
                                showCustomToast(getResources().getString(R.string.low_health),
                                        "android.resource://com.houbenz.android.lifesimulator/drawable/health","red");
                            }

                        }
                        if (hour >= 24) {
                            hour = 0;
                            minute = 0;
                            day++;

                            if(player.getDating().equals("true")) {
                                minusRelation += Params.MINUS_RELATION;
                            }

                            player.setBank_deposit(player.getBank_deposit() * 1.01f);


                            if (player.getStore_income() != 0) {

                                player.setBalance(player.getBalance() + player.getStore_income());
                                balance.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(() -> {
                                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.money_gain);
                                    mp.start();
                                    balance.setText(player.getBalance() + "$");
                                    balance.animate().scaleX(1f).scaleY(1f).setDuration(300);
                                });

                                showCustomToast("+"+player.getStore_income()+getResources().getString(R.string.from_stores),"","green");
                            }
                            saveProgress();
                        }
                        time.setText(hourS + ":" + minuteS);
                        dayView.setText(getString(R.string.day)+" "+ dayS);


                        //when you click learn
                        if(learning){


                            fragmentManager=getSupportFragmentManager();
                            fragmentManager.popBackStack();



                            mainText.setVisibility(View.VISIBLE);
                            mainText.setText(getResources().getString(R.string.now_learning));

                            learn_time--;

                            enableAllButtons(false);

                            startWorking.setEnabled(false);

                            startWorking.setText(getResources().getString(R.string.learning)+" "+learn_time +"m");


                            if(learn_time <= 0){
                                learning=false;
                                learn_time=60;
                                enableAllButtons(true);

                                startWorking.setText(getString(R.string.startWork));

                                if(! player.getWork().equals(getString(R.string.none)))
                                    startWorking.setEnabled(true);

                                DegreeFragment degreeFragment = new DegreeFragment();
                                degreeFragment.setItemPosition(degreeItemPosition);
                                fragmentInsertionSecond(degreeFragment);

                                speedSeekBar.setProgress(0);
                                speed=Params.TIME_SPEED_NORMAL;
                            }

                        }


                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!threadRun)
                    break;


            }
        });
        thread.start();
    }


    private void animateProgressBar(@NonNull ProgressBar progressBar){
        progressBar.animate().rotation(-5f).scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(()->{

            progressBar.animate().rotation(5f).scaleX(1f).scaleY(1f).setDuration(150).withEndAction(()->{
                progressBar.animate().rotation(0f).setDuration(150);
            }); });
    }

    public void enableAllButtons(boolean enable){
        work.setEnabled(enable);
        buy.setEnabled(enable);
        sleep.setEnabled(enable);
        bank.setEnabled(enable);
        study.setEnabled(enable);
        homeButton.setEnabled(enable);
        instantDollarButton.setEnabled(enable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveProgress();
    }


    @Override
    protected void onResume() {
       // mRewardVideoAdDoubleIncome.resume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
      //  mRewardVideoAdDoubleIncome.destroy(this);
        super.onDestroy();
        threadRun=false;
    }

    @Override
    public void onBackPressed() {
        deselectButtons();
        workthreadRun=false;
        mp.release();
        mpIncome.release();
        super.onBackPressed();
        mainText.setText(getString(R.string.doing_nothing));
        mainText.setVisibility(View.VISIBLE);

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
            //imageToast.setBackgroundColor(getResources().getColor(R.color.white));
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);


        bank=findViewById(R.id.bank);
        levelBar=findViewById(R.id.levelProgressBar);
        levelNumber=findViewById(R.id.levelNumber);
        jobName=findViewById(R.id.jobName);
        time=findViewById(R.id.time);
        dayView=findViewById(R.id.dayView);
        caracterImg =findViewById(R.id.caracterImg);
        doubleEarn=findViewById(R.id.doubleEarn);

        playerN =findViewById(R.id.playerN);
        income=findViewById(R.id.income);
        balance=findViewById(R.id.balance);
        instantDollarButton=findViewById(R.id.instantDollarButton);

        mainText=findViewById(R.id.mainText);


        sharedPreferences=getApplicationContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);

        //for the car
        viewModelCars=ViewModelProviders.of(this).get(ViewModelCars.class);
        deliverCars();

        //for the gifts
        viewModelGift=ViewModelProviders.of(this).get(ViewModelGift.class);
        deliverGift();


        try {
            MobileAds.initialize(this, APP_ADS_ID);

        }catch (Exception e){
        }
       mRewardVideoAdDoubleIncome =MobileAds.getRewardedVideoAdInstance(this);
        mRewardVideoAdDoubleIncome.setRewardedVideoAdListener(this);
        loadRewardAd();



        mpIncome = MediaPlayer.create(this,R.raw.money_gain);
        mp =MediaPlayer.create(this,R.raw.level_up);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

         runClockThread();


         constraintLayout=findViewById(R.id.mainLayout);

         constraintLayout.setOnTouchListener(mOnTouchListener);


        slot =getIntent().getIntExtra("slotNumber",1);

            healthbar=findViewById(R.id.healthBar);
            energyBar=findViewById(R.id.energyBar);
            hungerBar=findViewById(R.id.hungerBar);

            healthpr=findViewById(R.id.healthpr);
            energypr=findViewById(R.id.energypr);
            hungerpr=findViewById(R.id.hungerpr);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            if (GoogleSignIn.hasPermissions(account)) {

                this.account = account;


                settingUpPopUpforAchievements();


            }
        }


        loadProgress();

        homeButton=findViewById(R.id.homeButton);

        homeButton.setOnClickListener( event->{

            deselectButtons();

            homeButton.setSelected(true);

            saveProgress();
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("slot",player.getId());
            intent.putExtra("minusRelation",minusRelation);
            threadRun=false;
            minusRelation=0;
            startActivity(intent);

            finish();

        });

        work=findViewById(R.id.work);
        work.setOnClickListener(v -> {

            deselectButtons();

            work.setSelected(true);

            workFragment=new WorkFragment();
            fragmentInsertionSecond(workFragment);

        });

        bank.setOnClickListener(v -> {

            deselectButtons();

            bank.setSelected(true);

            bankFragment=new BankFragment();
            Bundle bundle =new Bundle();
            bundle.putFloat("accountSum",(int)player.getBank_deposit());
            bundle.putFloat("balance",(int)player.getBalance());


            bankFragment.setArguments(bundle);

            fragmentInsertionSecond(bankFragment);

        });

        buy=findViewById(R.id.buy);
        buy.setOnClickListener(v -> {

            deselectButtons();

            buy.setSelected(true);

            buyFragment =new BuyFragment();
            fragmentInsertionSecond(buyFragment);

        });

        sleep=findViewById(R.id.sleep);
        sleep.setOnClickListener(v -> {

            deselectButtons();

            sleep.setSelected(true);

            SleepFragment sleepFragment =new SleepFragment();
            fragmentInsertionSecond(sleepFragment);
        });

        study=findViewById(R.id.study);
        study.setOnClickListener(v -> {

            deselectButtons();

            study.setSelected(true);

            insertStudyFragment();

        });


        playerN.setText(player.getName());

        balance.setText(player.getBalance()+"$");



        income.setText(player.getWork_income()+"$/"+getString(R.string.hour));


        //Init of progress bars
        initialiseProgressBars();

        // when the button start work is pressed
        startWorking=findViewById(R.id.startWorking);

        if(player.getWork().equals(getString(R.string.none))) {
            startWorking.setEnabled(false);
        }
        else {
            startWorking.setEnabled(true);
        }



        startWorking.setOnClickListener(v -> {


            if(duo % 2 ==0) {
                workthreadRun = true;

                deselectButtons();
                startWorking.setSelected(true);

                startWorking.setText(getString(R.string.stopworking));
                //startWorking.setTextColor(getResources().getColor(R.color.red));

                startWorkThread();

                mainText.setVisibility(View.VISIBLE);

                duo++;

                String firstTime=sharedPreferences.getString("firstTime","none");
                if(firstTime.equals("none"))
                showTuto(getString(R.string.time_speed),getString(R.string.usebar),R.id.speedSeekBar);


            }
            else {

                startWorking.setSelected(false);

                workthreadRun=false;

                enableAllButtons(true);
                speedSeekBar.setProgress(0);
                speed=Params.TIME_SPEED_NORMAL;
                startWorking.setText(getString(R.string.startwork));
                startWorking.setTextColor(getResources().getColor(R.color.green));

                String firstTime=sharedPreferences.getString("firstTime","none");
                if(firstTime.equals("none")) {
                    showTuto(getString(R.string.learning), getString(R.string.study_section), R.id.study);
                    sharedPreferences.edit().putString("firstTime", "finished").apply();
                }


                duo++;
            }
        });


        doubleEarn.setOnClickListener(v -> {

            if(mRewardVideoAdDoubleIncome.isLoaded()) {
                doubleEarnClicked=true;
                mRewardVideoAdDoubleIncome.show();
            }
        });

        instantDollarButton.setOnClickListener(v ->{

           if(mRewardVideoAdDoubleIncome.isLoaded()){
               doubleEarnClicked=false;
               mRewardVideoAdDoubleIncome.show();
           }

        });

        speedSeekBar=(SeekBar)findViewById(R.id.speedSeekBar);
        speedName=findViewById(R.id.speedName);


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

        String firstTime = sharedPreferences.getString("firstTime","none");

        if(firstTime.equals("none"))
        showTuto(getString(R.string.work),getString(R.string.work_session),R.id.work);


        saveToCloud();

    }

    private void settingUpPopUpforAchievements(){
        GamesClient gamesClient = Games.getGamesClient(this,account);
        gamesClient.setViewForPopups(findViewById(android.R.id.content));
        gamesClient.setGravityForPopups(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
    }

    private void submitScore (long score){

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account!= null && GoogleSignIn.hasPermissions(account))
        Games.getLeaderboardsClient(this,account)
                .submitScore(getString(R.string.leaderboard_score),score);

    }

    private void showTuto(String title,String contentText,int viewid ){
        new GuideView.Builder(this)
                .setTitle(title)
                .setTargetView(findViewById(viewid))
                .setContentText(contentText)
                //.setDismissType(DismissType.outside)
                .build()
                .show();
    }




    public void deselectButtons(){
        work.setSelected(false);
        study.setSelected(false);
        bank.setSelected(false);
        sleep.setSelected(false);
        buy.setSelected(false);
    }



    public void doubleEarnThread(){


        final double rewardIncome=player.getWork_income() * Params.NUMBER_OF_MULTI_INOCME;
        player.setWork_income(rewardIncome);
        doubleEarn.setEnabled(false);
        income.setText(player.getWork_income() + "$/"+getString(R.string.hour));

        Thread minusTime =new Thread(() -> {

            int hours = Params.NUMBER_OF_HOURS_BENEFIT;
            doubleEarnMinutes = hours * 60;

            while (doubleEarnMinutes >= 0) {
                try {
                    Thread.sleep(speed);
                        runOnUiThread(() ->{
                        doubleEarnMinutes--;
                        doubleEarn.setText( getString(R.string.minutes_left)+" " + doubleEarnMinutes + " m");

                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() ->{

                Player player1 = myAppDataBase.myDao().getPlayer(slot);
                player.setWork_income(player1.getWork_income());
                income.setText(player.getWork_income() + "$");
                doubleEarn.setEnabled(true);
                doubleEarn.setText(getString(R.string.doubleEarn));
                loadRewardAd();

            });

        });

        minusTime.start();
    }

    synchronized public void startWorkThread(){

        Thread thread =new Thread(() ->{

                remainingMinutes =0;
                while (workthreadRun) {
                    try {

                        Thread.sleep(speed);

                        runOnUiThread(() ->
                                {

                           if(workthreadRun) {

                                       enableAllButtons(false);

                                        fragmentManager = getSupportFragmentManager();
                                        fragmentManager.popBackStack();



                                if(remainingMinutes % 60 == 0) {


                                    //increment player balance with work pay
                                    player.setBalance((int)player.getWork_income() + player.getBalance());

                                    balance.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).withEndAction(() -> {
                                        mpIncome.start();
                                        balance.setText(player.getBalance() + "$");
                                        balance.animate().scaleX(1f).scaleY(1f).setDuration(150);
                                    });

                                    //reduce player energy
                                    energyBar.setProgress(energyBar.getProgress()-Params.ENERGY_LOSS);
                                    energypr.setText(energyBar.getProgress()+"/"+energyBar.getMax());

                                    //increment player progress for a level
                                    player.getLevel_object().setProgressLevel(player.getLevel_object().getProgressLevel()+Params.XP_GAIN);
                                    levelBar.setProgress(player.getLevel_object().getProgressLevel());
                                }

                                if(player.getLevel_object().getMaxProgress()<=player.getLevel_object().getProgressLevel()) {

                                    //when the player level_up !
                                    int actualProgress =player.getLevel_object().getProgressLevel()-player.getLevel_object().getMaxProgress();
                                    player.upgradeLevel();

                                    showCustomToast(getString(R.string.have_leveled_up)+" "+player.getLevel_object().getLevel(),"","gold");

                                    if(player.getLevel_object().getLevel() ==5) {
                                        if(account != null && GoogleSignIn.hasPermissions(account))
                                            Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_level_up_5));
                                    }

                                    if(player.getLevel_object().getLevel() == 10) {
                                        if(account != null && GoogleSignIn.hasPermissions(account))
                                            Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_level_up_10));
                                    }

                                    levelNumber.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).withEndAction(() -> {



                                        if(player.getLevel_object().getLevel()<10)
                                            levelNumber.setText(getString(R.string.level) +": 0"+ player.getLevel_object().getLevel());
                                        else
                                            levelNumber.setText(getString(R.string.level) +": "+ player.getLevel_object().getLevel());


                                        levelNumber.animate().scaleX(1f).scaleY(1f).setDuration(150);
                                        mp.start();
                                    });

                                    levelBar.setMax(player.getLevel_object().getMaxProgress());
                                    levelBar.setProgress(player.getLevel_object().getProgressLevel()+actualProgress);
                                }
                            }


                            //update the time spent on working
                            mainText.setText(getString(R.string.hv_work)+" "+getString(R.string.hour)+ " : "+(remainingMinutes / 60) +" ,"+getString(R.string.minute)
                            +" : "+(remainingMinutes % 60) +" "+getString(R.string.hv_made)+": "+(player.getWork_income() * (remainingMinutes / 60))+"$");
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    remainingMinutes+=1;
                }

        });

        thread.start();
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


        levelBar.setMax(player.getLevel_object().getMaxProgress());

        levelBar.setProgress(player.getLevel_object().getProgressLevel());

        if(player.getLevel_object().getLevel()<10)
            levelNumber.setText(getString(R.string.level)+": 0"+player.getLevel_object().getLevel());

        else
            levelNumber.setText(getString(R.string.level)+": "+player.getLevel_object().getLevel());

    };



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
    public void deliverMainFragment(int idOfFragment) {


        //todo
        //change the test of fragment from name to id
        if(idOfFragment == 1 ){

        FoodFragment foodFragment =new FoodFragment();
        fragmentInsertionSecond(foodFragment);

        }
        if(idOfFragment == 2 ) {

            FournitureFragment fournitureFragment =new FournitureFragment();
            fragmentInsertionSecond(fournitureFragment);

        }
        if(idOfFragment == 3) {

            HouseFragment houseFragment =new HouseFragment();
            fragmentInsertionSecond(houseFragment);
        }

        if(idOfFragment == 4 ){
            StoreFragment storeFragment = new StoreFragment();
            fragmentInsertionSecond(storeFragment);
        }

        if(idOfFragment == 5 ){
            PharmacyFragment pharmacyFragment = new PharmacyFragment();
            fragmentInsertionSecond(pharmacyFragment);
        }

        if(idOfFragment == 6 ){
            CarFragment carFragment = new CarFragment();
            fragmentInsertionSecond(carFragment);
        }

        if(idOfFragment == 7 ){
             GiftFragment giftFragment = new GiftFragment();
            fragmentInsertionSecond(giftFragment);
        }
    }

    public void deliverGift(){
        viewModelGift.getGift().observe(this,gift -> {

            double newBalance= player.getBalance() - gift.getPrice();

            if(newBalance >=0){
                player.setBalance(newBalance );
                balance.setText(newBalance+"$");
                showCustomToast(getString(R.string.you_bought)+" "+gift.getName(),gift.getImgUrl(),"green");


                int giftCount =gift.getGiftCount()+1;

                if(gift.getId()==1){
                    gift.setGiftCount(giftCount);
                    myAppDataBase.myDao().updateGift(gift);
                }

                if(gift.getId()==2){
                    gift.setGiftCount(giftCount);
                    myAppDataBase.myDao().updateGift(gift);
                }

                if(gift.getId()==3){
                    gift.setGiftCount(giftCount);
                    myAppDataBase.myDao().updateGift(gift);
                }
                //to increment the number of gifts
                fragmentInsertionSecond(new GiftFragment());

            }
            else{
                showCustomToast(getString(R.string.not_enough_money)+" "+gift.getName(),gift.getImgUrl(),"red");
            }
        });
    }

    public void deliverCars(){

        viewModelCars.getCar().observe(this,car -> {

            Acquired_Houses acquired_garage = myAppDataBase.myDao().getAcqHouse(player.getId(),2);
            Acquired_Houses acquired_secondhome = myAppDataBase.myDao().getAcqHouse(player.getId(),3);

            if(acquired_garage == null && acquired_secondhome==null){


                House garage = myAppDataBase.myDao().getHouse(2);
                House house2 = myAppDataBase.myDao().getHouse(3);
                showCustomToast(getString(R.string.must_buy)+" "+garage.getName()+" or "+house2.getName()+ getString(R.string.first)+" ! ",garage.getImgUrl(),"red");
            }
            else {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog);
                Button confirm = dialog.findViewById(R.id.confirm);
                Button decline = dialog.findViewById(R.id.decline);

                double newBalance = player.getBalance() - car.getPrice();

                Acquired_Cars getACq = myAppDataBase.myDao().getAcquiredCars(player.getId(), car.getId());

                if (newBalance >= 0 && getACq == null) {

                    confirm.setOnClickListener(view -> {

                        if(account != null && GoogleSignIn.hasPermissions(account))
                         Games.getAchievementsClient(this,account).unlock(getString(R.string.achievement_your_first_car));


                        player.setBalance(newBalance);
                        balance.setText(player.getBalance() + "$");
                        showCustomToast(getString(R.string.you_bought)+" " + car.getName(), car.getImgUrl(), "green");
                        Acquired_Cars acquired_cars = new Acquired_Cars();
                        acquired_cars.setCar_id(car.getId());
                        acquired_cars.setPlayer_id(player.getId());
                        acquired_cars.setImgUrl(car.getImgUrl());
                        acquired_cars.setAvailable("true");
                        myAppDataBase.myDao().addAcquired_Car(acquired_cars);
                        CarFragment carFragment = new CarFragment();
                        fragmentInsertionSecond(carFragment);
                        dialog.dismiss();
                    });
                    dialog.show();

                } else {
                    if (getACq != null) {

                        showCustomToast(car.getName() + getString(R.string.is_selected)+" ", car.getImgUrl(), "green");


                        List<Acquired_Cars> acquired_cars = myAppDataBase.myDao().getAcquiredCars(player.getId());
                        for (Acquired_Cars acq : acquired_cars) {
                            acq.setAvailable("false");
                            myAppDataBase.myDao().updateAcquired_car(acq);

                        }
                        getACq.setAvailable("true");
                        myAppDataBase.myDao().updateAcquired_car(getACq);

                    } else
                        showCustomToast(getString(R.string.not_enough_money)+" " + car.getName(), car.getImgUrl(), "red");
                }

                decline.setOnClickListener(view1 -> {
                    dialog.dismiss();
                });
            }
        });

    }




    //To execute tasks from fragments
    @Override
    public void deliverWork(Work work) {

        String firstTime = sharedPreferences.getString("firstTime","none");

        Log.i("okkk","firstTime : "+firstTime);

        Log.i("okkk","firstTime : "+sharedPreferences.getString("firstTime","none"));

        if(firstTime.equals("none")){
            showTuto(getString(R.string.startWork),getString(R.string.begin_work),R.id.startWorking);

        }

        if(!work.getName().equals(player.getWork()))
        {

            if(account != null && GoogleSignIn.hasPermissions(account)){
                Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_second_job));
            }

            income=findViewById(R.id.income);

        income.setText(work.getIncome()+"$/"+getString(R.string.hour));

        choosenWork =work;

        player.setWork(choosenWork.getName());
        player.setWork_time(choosenWork.getWork_time());
        player.setWork_image_path(work.getImgPath());
        player.setWork_income(work.getIncome());

        myAppDataBase.myDao().updatePlayer(player);


        Uri imgURI = Uri.parse(work.getImgPath());
        caracterImg.setImageURI(imgURI);
        saveProgress();
        startWorking.setEnabled(true);
        jobName.setText(work.getName());
        saveToCloud();

        showCustomToast(getString(R.string.youre_now)+" "+work.getName(),work.getImgPath(),"green");

        if (work.getId() ==8){

            if(account != null && GoogleSignIn.hasPermissions(account))
                Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_einstein));
        }


        //replaced admob one
        if(mRewardVideoAdDoubleIncome.isLoaded())
            doubleEarn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void deliverFourniture(final Furniture fourniture) {


        Acquired_Houses acquired_home = myAppDataBase.myDao().getAcqHouse(player.getId(),1);
        Acquired_Houses acquired_secondhome = myAppDataBase.myDao().getAcqHouse(player.getId(),3);

        if (   acquired_home == null && acquired_secondhome == null ){

            House garage = myAppDataBase.myDao().getHouse(1);
            House house2 = myAppDataBase.myDao().getHouse(3);

            showCustomToast(getString(R.string.must_buy)+" "+garage.getName()+" or "
                    +house2.getName()+" "+getString(R.string.first)+" ! ",garage.getImgUrl(),"red");
        }else {
            Acquired_Furnitures acquired_furnitures1 =myAppDataBase.myDao().getAcqFurn(fourniture.getId(),player.getId());
            // test if is the furniture is already bought
            if(acquired_furnitures1 == null){


                Dialog dialog = new Dialog(GameScene.this);



                dialog.setContentView(R.layout.dialog);

                TextView title = dialog.findViewById(R.id.dialogTitle);
                Button confirm=dialog.findViewById(R.id.confirm);
                Button decline =dialog.findViewById(R.id.decline);


                title.setText(getString(R.string.purchase_item));

                confirm.setOnClickListener(view ->{
                    double newBalance=player.getBalance()-fourniture.getPrice();

                    String color;
                    String message;
                    if(newBalance>=0) {
                        player.setBalance(newBalance);
                        balance.setText(newBalance + "$");
                        message=getString(R.string.you_purchased)+" "+fourniture.getName()+" "
                                +getString(R.string._for)+" "+fourniture.getPrice()+"$ !";
                        color="green";

                        Acquired_Furnitures acquired_furnitures = new Acquired_Furnitures();
                        acquired_furnitures.setAvailable("true");
                        acquired_furnitures.setFurn_id(fourniture.getId());
                        acquired_furnitures.setPlayer_id(player.getId());
                        acquired_furnitures.setFurnitureType(fourniture.getFurnitureType());
                        acquired_furnitures.setImgurl(fourniture.getImgUrl());

                        myAppDataBase.myDao().addAcquired_Furniture(acquired_furnitures);

                        // viewmodel.getAcquired_furn().getValue().add(acquired_furnitures);
                        dialog.dismiss();
                        dialog.cancel();

                        if(account != null && GoogleSignIn.hasPermissions(account))
                            Games.getAchievementsClient(getApplicationContext(),account)
                                    .unlock(getString(R.string.achievement_first_furniture));


                        if(account != null && GoogleSignIn.hasPermissions(account))
                            Games.getAchievementsClient(getApplicationContext(),account)
                                    .increment(getString(R.string.achievement_a_lot_of_furnitures),1);

                    }else{
                        message = getString(R.string.insufficiant_funds)+""+fourniture.getName();
                        color="red";
                        dialog.dismiss();
                        dialog.cancel();
                    }
                   // showPurchaseDialog(message);

                    showCustomToast(message,fourniture.getImgUrl(),color);
                    //this is for the view model between homefragment and GameScene

                    insertFurnitureFragment();
                });

                decline.setOnClickListener(view ->{
                    dialog.dismiss();
                    dialog.cancel();
                });

                dialog.show();


            }else {

              List <Acquired_Furnitures> acquired_furnitures = myAppDataBase.myDao().getAcquiredFurnitures(player.getId());

              for(Acquired_Furnitures furn : acquired_furnitures) {
                  if(furn.getFurnitureType().equals(fourniture.getFurnitureType()))
                  furn.setAvailable("false");
                  myAppDataBase.myDao().updateAcquired_Furnitures(furn);
              }

                Toast.makeText(getApplicationContext(), fourniture.getName() + " "+getString(R.string.is_now_used), Toast.LENGTH_SHORT).show();

                acquired_furnitures1.setAvailable("true");

                myAppDataBase.myDao().updateAcquired_Furnitures(acquired_furnitures1);

                //viewmodel.getAcquired_furn().getValue().add(acquired_furnitures1);

            }
        }
    }

    @Override
    public void deliverSleep(int hoursNumber) {

        sleepTime=hoursNumber;

        if(sleepTime >0) {

            energyBar.setProgress(hoursNumber * Params.ENERGY_GAIN_PER_HOUR + energyBar.getProgress());
            energypr.setText(energyBar.getProgress() + "/" + energyBar.getMax());

            hungerBar.setProgress(hungerBar.getProgress() - hoursNumber * Params.HUNGER_LOSS_PER_HOUR_IN_SLEEP);
            hungerpr.setText(hungerBar.getProgress() + "/" + hungerBar.getMax());

            if (hungerBar.getProgress() == 0) {
                healthbar.setProgress(healthbar.getProgress() - hoursNumber * Params.HEALTH_LOSS_PER_HOUR_IN_SLEEP);
                healthpr.setText(healthbar.getProgress() + "/" + healthbar.getMax());
            }

            hour += sleepTime;

            if (hour >= 24) {
                hour -= 24;
                day++;
                player.setBank_deposit(player.getBank_deposit() * 1.01f);
                saveProgress();
                if (player.getStore_income() != 0) {

                    player.setBalance(player.getBalance() + player.getStore_income());
                    balance.setText(player.getBalance() + "$");

                    showCustomToast("+"+player.getStore_income()+getString(R.string.from_stores),"","green");
                }
            }
            switcher = findViewById(R.id.switcher);

            switcher.animate().alpha(0f).setDuration(250).withEndAction(() -> {
                switcher.animate().setDuration(250).alpha(1.0f);
                switcher.showNext();
            });

            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {

                    switcher.animate().alpha(0f).setDuration(250).withEndAction(() -> {
                        switcher.animate().alpha(1.0f).setDuration(250);
                        switcher.showPrevious();
                    });
                }
            };
            countDownTimer.start();
        }
    }

    @Override
    public void deliverFood(Food food){

        double newBalance =player.getBalance()-food.getPrice();
        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");
            hungerBar.setProgress(food.getBenefit()+hungerBar.getProgress());
            hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());

            if (food.getId() == 2){
                if(account != null && GoogleSignIn.hasPermissions(account))
                Games.getAchievementsClient(getApplicationContext(),account).increment(getString(R.string.achievement_chiken_dinner),1);
            }
        }
        else{

            showCustomToast(getString(R.string.insufficiant_funds)+" "+food.getName(),food.getImgUrl(),"red");
        }
    }

    @Override
    public void deliverMedicine(Medicine medicine) {

        healthbar.setProgress(healthbar.getProgress()+medicine.getBenefit());
        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());

        double newBalance=player.getBalance()-medicine.getPrice();
        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");
        }else
            showCustomToast(getString(R.string.insufficiant_funds)+" "+medicine.getName(),medicine.getImgUrl(),"red");
    }

    @Override
    public void deliverHouse(House house) {

            Acquired_Houses acquired_house1 = myAppDataBase.myDao().getAcqHouse(player.getId(), house.getId());

            if (acquired_house1 == null) {
                double newBalance = player.getBalance() - house.getPrice();

                if (newBalance >= 0) {
                    player.setBalance(newBalance);
                    balance.setText(player.getBalance() + "$");

                    Acquired_Houses acquired_houses = new Acquired_Houses();
                    acquired_houses.setHouse_id(house.getId());
                    acquired_houses.setPlayer_id(player.getId());
                    acquired_houses.setImgUrl(house.getImgUrl());

                    myAppDataBase.myDao().addAcquired_House(acquired_houses);

                    showCustomToast(getString(R.string.congratulation_u_bought)+" " + house.getName(), house.getImgUrl(), "green");

                    if(house.getId() == 1) {
                        if (account != null && GoogleSignIn.hasPermissions(account))
                            Games.getAchievementsClient(getApplicationContext(), account).unlock(getString(R.string.achievement_ma_house));
                    }

                    if(house.getId() == 3) {
                        if (account != null && GoogleSignIn.hasPermissions(account))
                            Games.getAchievementsClient(getApplicationContext(), account).unlock(getString(R.string.achievement_comfy_house));
                    }

                } else
                    showCustomToast(getString(R.string.insufficiant_funds)+" " + house.getName(), house.getImgUrl(), "red");


                HouseFragment houseFragment = new HouseFragment();
                fragmentInsertionSecond(houseFragment);
            }
    }

    public  double getIncomeFromStore(){

        List<Acquired_Stores> acquired_stores =myAppDataBase.myDao().getAcquiredStores(player.getId());
        double income =0;

        if(!acquired_stores.isEmpty()){
            for (Acquired_Stores acq : acquired_stores){
                income += myAppDataBase.myDao().getStoreIncome(acq.getStore_id());
            }
        }

        return  income;
    }


    @Override
    public void deliverStore(Store store) {

        double newBalance = player.getBalance()-store.getPrice();

        Dialog dialog = new Dialog(GameScene.this);

        dialog.setContentView(R.layout.dialog);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        Button confirm=dialog.findViewById(R.id.confirm);
        Button decline =dialog.findViewById(R.id.decline);

        if(newBalance>=0){

            confirm.setOnClickListener(view ->{


                player.setBalance(newBalance);
                balance.setText(player.getBalance()+"$");

                Acquired_Stores acquired_stores = new Acquired_Stores();
                acquired_stores.setStore_id(store.getId());
                acquired_stores.setPlayer_id(player.getId());
                dialog.dismiss();
                dialog.cancel();

                myAppDataBase.myDao().addAcquired_Store(acquired_stores);

                player.setStore_income(getIncomeFromStore());
                showCustomToast(getString(R.string.congratulation_u_bought)+" "+store.getName()+" !",store.getImgUrl(),"green");

                insertStoreFragment();

                if(account != null && GoogleSignIn.hasPermissions(account))
                Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_first_store));


                if(store.getId() == 4) {
                    if (account != null && GoogleSignIn.hasPermissions(account))
                        Games.getAchievementsClient(getApplicationContext(), account).unlock(getString(R.string.achievement_supermaket_wow));
                }

            });
            decline.setOnClickListener(view ->{
                dialog.dismiss();
                dialog.cancel();
            });

            dialog.show();

        }else {
            showCustomToast(getString(R.string.insufficiant_funds)+" "+store.getName(),store.getImgUrl(),"red");
            dialog.dismiss();
            dialog.cancel();
        }
    }

    private int degree_y=0;
    private int degreeItemPosition =0;
    @Override
    public void deliverDegreeItemPosition(int pos,int y) {
        degreeItemPosition =pos;
        degree_y=y;
    }

    @Override
    public void deliverDegree(Degree degree) {

        double newBalance =player.getBalance()-degree.getPrice();


        Acquired_degree acq =myAppDataBase.myDao().getAcqDegr(player.getId(),degree.getId());

        if(acq == null) {

        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");

            Acquired_degree acquired_degree = new Acquired_degree();
            acquired_degree.setPlayer_id(player.getId());
            acquired_degree.setDegree_id(degree.getId());
            acquired_degree.setDegree_Name(degree.getName());
            acquired_degree.setAvailable("false");
            acquired_degree.setPlayer_progress(acquired_degree.getPlayer_progress() + 5);
            myAppDataBase.myDao().addAcquired_degree(acquired_degree);

            learning=true;

        }else
            {
                showCustomToast(getString(R.string.insufficiant_funds)+" "+degree.getName(),"","red");
            }
        }
            else {

            if(newBalance >=0) {
                acq.setPlayer_progress(acq.getPlayer_progress() + 5);
                player.setBalance(newBalance);
                balance.setText(player.getBalance() + "$");
                learning=true;

                mainText.setVisibility(View.VISIBLE);
                mainText.setText(getString(R.string.now_learning));
            }
            else
                showCustomToast(getString(R.string.insufficiant_funds)+" "+degree.getName(),"","red");

            if (!acq.getAvailable().equals("true"))
            {

                if (degree.getProgress() <= acq.getPlayer_progress()) {

                    acq.setAvailable("true");
                    showCustomToast(getString(R.string.purchase_of)+" "+degree.getName()+getString(R.string.done_success),"","green");

                    if(account != null && GoogleSignIn.hasPermissions(account))
                        Games.getAchievementsClient(getApplicationContext(),account).unlock(getString(R.string.achievement_first_degree));
                }

                myAppDataBase.myDao().update_Acquired_Degree(acq);
            }
        }

    }

    @Override
    public void depositAndWithdraw(String operation) {


        if(operation.equals("deposit")){

            Bundle bundle =new Bundle();
            bundle.putFloat("balance",(int)player.getBalance());
            bundle.putFloat("balanceInBank",(int)player.getBank_deposit());

            DepositFragment depositFragment =new DepositFragment();
            depositFragment.setArguments(bundle);

            fragmentInsertionSecond(depositFragment);

        }else {
            WithdrawFragment withdrawFragment =new WithdrawFragment();

            Bundle bundle =new Bundle();
            bundle.putFloat("balanceInBank",(int)player.getBank_deposit());
            withdrawFragment.setArguments(bundle);

            fragmentInsertionSecond(withdrawFragment);
        }

    }

    @Override
    public void deliverDeposit(double deposit){

        double newBalance =player.getBalance()-deposit;

        player.setBank_deposit(player.getBank_deposit()+deposit);
        player.setBalance(newBalance);
        balance.setText(player.getBalance()+"$");
        saveProgress();
        fragmentInsertionSecond(bankFragment);
    }

    @Override
    public void deliverWithdraw(double withdraw) {

        player.setBank_deposit(player.getBank_deposit()-withdraw);

        player.setBalance(player.getBalance()+withdraw);

        balance.setText(player.getBalance()+"$");
        saveProgress();
        fragmentInsertionSecond(bankFragment);
    }

    public void fragmentInsertionSecond(Fragment fragment){

        mainText.setVisibility(View.GONE);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction=fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);



        Bundle bundle = new Bundle();

        bundle.putFloat("balance",(int)player.getBalance());
        bundle.putFloat("balanceInBank",(int)player.getBank_deposit());
        bundle.putInt("playerLevel",player.getLevel_object().getLevel());
        bundle.putInt("slot",player.getId());
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.placefragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void insertFurnitureFragment(){
        FournitureFragment fournitureFragment = new FournitureFragment();
        fragmentInsertionSecond(fournitureFragment);
    }

    public void insertHomeFragment(){

        homeFragment =new HomeFragment();
        fragmentInsertionSecond(homeFragment);
    }

    public void insertStudyFragment() {
        DegreeFragment degreeFragment = new DegreeFragment();
        fragmentInsertionSecond(degreeFragment);
    }

    public void insertStoreFragment() {
        StoreFragment storeFragment = new StoreFragment();
        fragmentInsertionSecond(storeFragment);
    }

    public void insertHouseFragment(){
        HouseFragment houseFragment=new HouseFragment();
        fragmentInsertionSecond(houseFragment);
    }


    private void loadRewardAd(){
            mRewardVideoAdDoubleIncome.loadAd(AD_VIDEO_ID, new AdRequest.Builder().build());

    }


    @Override
    public void onRewardedVideoAdLoaded() {

        if(!player.getWork().equals(getString(R.string.none)))
            doubleEarn.setVisibility(View.VISIBLE);

        instantDollarButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onRewardedVideoAdOpened() { }
    @Override
    public void onRewardedVideoStarted() { }
    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardAd();
        instantDollarButton.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onRewarded(RewardItem rewardItem) {
        if(doubleEarnClicked) {
            doubleEarnThread();
            showCustomToast(getString(R.string.now_earn_double)+" "+ player.getWork_income()+" x2","","green");
        }
        else{
            player.setBalance(player.getBalance()+100);
            balance.setText(player.getBalance()+"$");
            showCustomToast("100$","","green");
        }
    }
    @Override
    public void onRewardedVideoAdLeftApplication() { }
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardAd();
    }
    @Override
    public void onRewardedVideoCompleted() { }


    public void loadProgress(){
        database.Player loaded_player = myAppDataBase.myDao().getPlayer(slot);
        jobName.setText(loaded_player.getWork());
        caracterImg.setImageURI(Uri.parse(loaded_player.getWork_image_path()));
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

        double workincome = myAppDataBase.myDao().work_incorme(player.getWork());

        player.setWork_income(workincome);

        myAppDataBase.myDao().updatePlayer(player);
    }

    private void saveToCloud(){

        OneTimeWorkRequest oneTimeWorkRequest =new OneTimeWorkRequest.Builder(SaveToCloudWork.class)
                .build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        submitScore((long)player.getBalance());
    }

}
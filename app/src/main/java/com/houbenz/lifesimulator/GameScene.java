package com.houbenz.lifesimulator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import java.util.List;

import database.Acquired_Cars;
import database.Acquired_Furnitures;
import database.Food;
import database.Furniture;
import beans.House;
import beans.Level;
import database.Medicine;
import database.Acquired_Stores;
import database.Acquired_degree;
import database.Degree;
import database.Player;
import database.Store;
import database.Work;
import conf.Params;
import fragments.BankFragment;
import fragments.BuyFragment;
import fragments.CarFragment;
import fragments.DepositFragment;
import fragments.FoodFragment;
import fragments.FournitureFragment;
import fragments.HouseFragment;
import fragments.HomeFragment;
import fragments.DegreeFragment;
import fragments.PharmacyFragment;
import fragments.SleepFragment;
import fragments.StoreFragment;
import fragments.WithdrawFragment;
import fragments.WorkFragment;
import viewmodels.ViewModelCars;


public class GameScene extends AppCompatActivity
        implements WorkFragment.onWorkSelected, BuyFragment.OnMainFragmentClicked,FournitureFragment.OnFournitureClicked
    ,SleepFragment.onSleepClicked,FoodFragment.onFoodClicked, PharmacyFragment.OnMedicineClicked,
        HouseFragment.OnHouseClicked, StoreFragment.OnStoreClicked, HomeFragment.homeShow
    , DegreeFragment.OnDegreeClick, BankFragment.OnDeposit,WithdrawFragment.OnWithdraw,DepositFragment.OnDeposit,RewardedVideoAdListener
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

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;

    private TextView time ;
    private TextView dayView ;
    private TextView playerN;
    private ImageView caracterImg ;
    private TextView jobName;

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


    private RewardedVideoAd mRewardVideoAd;

    private ConstraintLayout constraintLayout;

    private ViewModelCars viewModelCars;



    private boolean learning =false;
    private int learn_time=Params.LEARN_TIME;

    //this is the original app ad id
    private final static  String APP_ADS_ID = "ca-app-pub-5859725902066144~3681738021";

    //this is test app ad id
    //private final static  String APP_ADS_ID = "ca-app-pub-3940256099942544~3347511713";

     //this is the original video ad id
     private final static String AD_VIDEO_ID = "ca-app-pub-5859725902066144/4392184462";

    //this is for test video ad
    //private final static String AD_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";


    private View.OnTouchListener mOnTouchListener = (v , event) -> {

            hideSystemUI();
            return false;

    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void runClockThread(){

        final Thread thread =new Thread(() -> {

            while (threadRun) {

                try {

                    Thread.sleep(speed);
                    runOnUiThread(() -> {

                        //For GAME OVER
                        if(healthbar.getProgress()<=0){
                            threadRun=false;
                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_toast_red);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView message= dialog.findViewById(R.id.message);

                            message.setText("Game Over ! :( your health is Zero");
                            dialog.setOnDismissListener( dialog1 -> {
                                dialog1.dismiss();
                                finish();
                            });


                            //alertDialog.show();
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
                            hungerBar.setProgress(hungerBar.getProgress()-Params.HUNGER_LOSS_PER_HOUR);
                            hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());

                            ///

                            if(hungerBar.getProgress()==0 && energyBar.getProgress()==0){

                                healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR - Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());

                            }else {
                                if(hungerBar.getProgress()==0 && energyBar.getProgress()>0){

                                    healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR );
                                    healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
                                }else{
                                    if(hungerBar.getProgress()>0 && energyBar.getProgress()==0){
                                        healthbar.setProgress(healthbar.getProgress() -Params.HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY);
                                        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
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
                            saveProgress();
                        }
                        time.setText(hourS + ":" + minuteS);
                        dayView.setText(getString(R.string.day)+" "+ dayS);


                        //when you click learn
                        if(learning){


                            fragmentManager=getSupportFragmentManager();
                            fragmentManager.popBackStack();


                            learn_time--;
                            work.setEnabled(false);
                            buy.setEnabled(false);
                            sleep.setEnabled(false);
                            bank.setEnabled(false);
                            study.setEnabled(false);
                            startWorking.setEnabled(false);

                            startWorking.setText("Learning "+learn_time +"m");

                            if(learn_time <= 0){
                                learning=false;
                                learn_time=60;

                                work.setEnabled(true);
                                buy.setEnabled(true);
                                sleep.setEnabled(true);
                                bank.setEnabled(true);
                                study.setEnabled(true);
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

    @Override
    protected void onStop() {
        super.onStop();
        saveProgress();
    }


    @Override
    protected void onResume() {
        mRewardVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mRewardVideoAd.destroy(this);
        super.onDestroy();
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
           default:;
       }


        TextView messageTextView = layout.findViewById(R.id.message);
        ImageView imageToast = layout.findViewById(R.id.imageToast);

        imageToast.setVisibility(View.GONE);
        messageTextView.setText(message);

        if (!imgUrl.equals("")) {
            imageToast.setVisibility(View.VISIBLE);
            imageToast.setImageURI(Uri.parse(imgUrl));
            imageToast.setBackgroundColor(getResources().getColor(R.color.white));
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


        //for the car
        viewModelCars=ViewModelProviders.of(this).get(ViewModelCars.class);
        deliverCars();

        try {
            MobileAds.initialize(this, APP_ADS_ID);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"not loading :/",Toast.LENGTH_LONG).show();
        }
        mRewardVideoAd =MobileAds.getRewardedVideoAdInstance(this);
        mRewardVideoAd.setRewardedVideoAdListener(this);
        loadRewardAd();
        //listenReward();


        setVolumeControlStream(AudioManager.STREAM_MUSIC);

         runClockThread();


         constraintLayout=findViewById(R.id.mainLayout);

         constraintLayout.setOnTouchListener(mOnTouchListener);


        slot =getIntent().getIntExtra("slotNumber",0);

            healthbar=findViewById(R.id.healthBar);
            energyBar=findViewById(R.id.energyBar);
            hungerBar=findViewById(R.id.hungerBar);

            healthpr=findViewById(R.id.healthpr);
            energypr=findViewById(R.id.energypr);
            hungerpr=findViewById(R.id.hungerpr);



        loadProgress();


        insertHomeFragment();


        homeButton=findViewById(R.id.homeButton);

        homeButton.setOnClickListener( event->{
            insertHomeFragment();
        });

        work=findViewById(R.id.work);
        work.setOnClickListener(v -> {

            workFragment=new WorkFragment();
            fragmentInsertionSecond(workFragment);

        });


        bank.setOnClickListener(v -> {
            bankFragment=new BankFragment();
            Bundle bundle =new Bundle();
            bundle.putFloat("accountSum",(int)player.getBank_deposit());
            bundle.putFloat("balance",(int)player.getBalance());


            bankFragment.setArguments(bundle);

            fragmentInsertionSecond(bankFragment);

        });

        buy=findViewById(R.id.buy);
        buy.setOnClickListener(v -> {

            buyFragment =new BuyFragment();
            fragmentInsertionSecond(buyFragment);

        });

        sleep=findViewById(R.id.sleep);
        sleep.setOnClickListener(v -> {

            SleepFragment sleepFragment =new SleepFragment();
            fragmentInsertionSecond(sleepFragment);
        });

        study=findViewById(R.id.study);
        study.setOnClickListener(v -> insertStudyFragment());


        playerN.setText(player.getName());

        balance.setText(player.getBalance()+"$");



        income.setText(player.getWork_income()+"$/"+getString(R.string.day));


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


        startWorking.setOnClickListener(v -> startWorkThread());


        doubleEarn.setOnClickListener(v -> {
            if(mRewardVideoAd.isLoaded()){
                mRewardVideoAd.show();
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
    }


    public void doubleEarnThread(){


        final double rewardIncome=player.getWork_income() * Params.NUMBER_OF_MULTI_INOCME;
        player.setWork_income(rewardIncome);
        doubleEarn.setEnabled(false);
        income.setText(player.getWork_income() + "$");

        Thread minusTime =new Thread(() -> {

            int hours = Params.NUMBER_OF_HOURS_BENEFIT;
            doubleEarnMinutes = hours * 60;

            while (doubleEarnMinutes >= 0) {
                try {
                    Thread.sleep(speed);
                        runOnUiThread(() ->{
                        doubleEarnMinutes--;
                        doubleEarn.setText("minutes left " + doubleEarnMinutes + " m");

                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() ->{
                double normalIncome = player.getWork_income() / Params.NUMBER_OF_MULTI_INOCME;
                player.setWork_income(normalIncome);
                income.setText(player.getWork_income() + "$");
                doubleEarn.setEnabled(true);
                doubleEarn.setText(getString(R.string.doubleEarn));
                loadRewardAd();

            });

        });

        minusTime.start();
    }

    public void startWorkThread(){

        Thread thread =new Thread(() ->{
                 remainingMinutes =player.getWork_time()*60;
                while (remainingMinutes>0) {

                    balance = findViewById(R.id.balance);

                    try {

                        Thread.sleep(speed);

                        runOnUiThread(() ->
                                {
                                if(ignore) {
                                    startWorking.setEnabled(false);
                                    startWorking.setText(getString(R.string.onWork));
                                    work.setEnabled(false);
                                    buy.setEnabled(false);
                                    sleep.setEnabled(false);
                                    bank.setEnabled(false);
                                    study.setEnabled(false);

                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.popBackStack();
                                }

                                ignore=false;
                                if(remainingMinutes % 60 == 0) {


                                    //increment player balance with work pay
                                    player.setBalance((int)player.getWork_income() + player.getBalance());

                                    balance.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(() -> {
                                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.money_gain);
                                        mp.start();
                                        balance.setText(player.getBalance() + "$");
                                        balance.animate().scaleX(1f).scaleY(1f).setDuration(300);
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

                                    showCustomToast("You've Leveled up "+player.getLevel_object().getLevel(),"","gold");



                                    levelNumber.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(() -> {


                                        MediaPlayer mp =MediaPlayer.create(getApplicationContext(),R.raw.level_up);


                                        if(player.getLevel_object().getLevel()<10)
                                            levelNumber.setText(getString(R.string.level) +": 0"+ player.getLevel_object().getLevel());
                                        else
                                            levelNumber.setText(getString(R.string.level) +": "+ player.getLevel_object().getLevel());


                                        levelNumber.animate().scaleX(1f).scaleY(1f).setDuration(300);
                                        mp.start();
                                    });

                                    levelBar.setMax(player.getLevel_object().getMaxProgress());
                                    levelBar.setProgress(player.getLevel_object().getProgressLevel()+actualProgress);

                                }
                                if(remainingMinutes==0){
                                    startWorking.setEnabled(true);
                                    buy.setEnabled(true);
                                    sleep.setEnabled(true);
                                    bank.setEnabled(true);
                                    study.setEnabled(true);
                                    startWorking.setText(getString(R.string.startwork));
                                    work.setEnabled(true);
                                    speedSeekBar.setProgress(0);
                                    speed=Params.TIME_SPEED_NORMAL;

                                    ignore=true;
                                }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    remainingMinutes-=1;
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
    public void deliverMainFragment(String nameOfFragment) {


        if(nameOfFragment.equals("Food") ||nameOfFragment.equals("Nourriture") ){

        FoodFragment foodFragment =new FoodFragment();
        fragmentInsertionSecond(foodFragment);

        }
        if(nameOfFragment.equals("Furniture")||nameOfFragment.equals("Fourniture")) {

            FournitureFragment fournitureFragment =new FournitureFragment();
            fragmentInsertionSecond(fournitureFragment);

        }
        if(nameOfFragment.equals("House") ||nameOfFragment.equals("Maison")) {

            HouseFragment houseFragment =new HouseFragment();
            fragmentInsertionSecond(houseFragment);
        }

        if(nameOfFragment.equals("Store") ||nameOfFragment.equals("Magasin")){
            StoreFragment storeFragment = new StoreFragment();
            fragmentInsertionSecond(storeFragment);
        }

        if(nameOfFragment.equals("Pharmacy") || nameOfFragment.equals("Pharmacie")){
            PharmacyFragment pharmacyFragment = new PharmacyFragment();
            fragmentInsertionSecond(pharmacyFragment);
        }

        if(nameOfFragment.equals("Cars") || nameOfFragment.equals("Voitures")){
            CarFragment carFragment = new CarFragment();
            fragmentInsertionSecond(carFragment);
        }
    }




    public void deliverCars(){
        viewModelCars.getCar().observe(this,car -> {

            double newBalance=player.getBalance()-car.getPrice();


            Acquired_Cars getACq = MainMenu.myAppDataBase.myDao().getAcquiredCars(player.getId(),car.getId());
            if(newBalance >= 0 && getACq == null){

                player.setBalance(newBalance);
                balance.setText(player.getBalance()+"$");

                showCustomToast("you bought "+car.getName(),car.getImgUrl(),"green");

                Acquired_Cars acquired_cars = new Acquired_Cars();
                acquired_cars.setCar_id(car.getId());
                acquired_cars.setPlayer_id(player.getId());
                MainMenu.myAppDataBase.myDao().addAcquired_Car(acquired_cars);
            }
            else{
                if(getACq!= null)
                    showCustomToast("you already own this car",car.getImgUrl(),"red");
                else
                    showCustomToast("Not enough money to buy "+car.getName(),car.getImgUrl(),"red");
            }
        });

        CarFragment carFragment = new CarFragment();
        fragmentInsertionSecond(carFragment);
    }

    //To execute tasks from fragments
    @Override
    public void deliverWork(Work work) {


        income=findViewById(R.id.income);
        income.setText(work.getIncome()+"$/"+getString(R.string.day));

        choosenWork =work;

        player.setWork(choosenWork.getName());
        player.setWork_time(choosenWork.getWork_time());
        player.setWork_image_path(work.getImgPath());
        player.setWork_income(work.getIncome());

        Uri imgURI = Uri.parse(work.getImgPath());
        caracterImg.setImageURI(imgURI);
        saveProgress();
        startWorking.setEnabled(true);
        jobName.setText(work.getName());

        if(mRewardVideoAd.isLoaded())
            doubleEarn.setVisibility(View.VISIBLE);


    }

    @Override
    public void deliverFourniture(final Furniture fourniture) {

        Acquired_Furnitures acquired_furnitures1 =MainMenu.myAppDataBase.myDao().getAcqFurn(fourniture.getId(),player.getId());
        // test if is the furniture is already bought
        if(acquired_furnitures1 == null){


            Dialog dialog = new Dialog(GameScene.this);



            dialog.setContentView(R.layout.dialog);

            TextView title = dialog.findViewById(R.id.dialogTitle);
            Button confirm=dialog.findViewById(R.id.confirm);
            Button decline =dialog.findViewById(R.id.decline);


            title.setText("Would you like to purchase this item ?");

            confirm.setOnClickListener(view ->{
                double newBalance=player.getBalance()-fourniture.getPrice();

                boolean green = false ;

                String message;
                if(newBalance>=0) {
                    player.setBalance(newBalance);
                    balance.setText(newBalance + "$");
                    message=" you purchased a "+fourniture.getName()+" for "+fourniture.getPrice()+"$ !";

                    Acquired_Furnitures acquired_furnitures = new Acquired_Furnitures();
                    acquired_furnitures.setAvailable("true");
                    acquired_furnitures.setFurn_id(fourniture.getId());
                    acquired_furnitures.setPlayer_id(player.getId());
                    acquired_furnitures.setFurnitureType(fourniture.getFurnitureType());
                    acquired_furnitures.setImgurl(fourniture.getImgUrl());

                    MainMenu.myAppDataBase.myDao().addAcquired_Furniture(acquired_furnitures);

                    // viewmodel.getAcquired_furn().getValue().add(acquired_furnitures);
                    dialog.dismiss();
                    dialog.cancel();

                    green=true;

                }else{
                    message =" insufficiant funds to purchase "+fourniture.getName();
                    dialog.dismiss();
                    dialog.cancel();
                }
               // showPurchaseDialog(message);

                showCustomToast(message,fourniture.getImgUrl(),"green");


                //this is for the view model between homefragment and GameScene

                insertFurnitureFragment();

                onHomeShow(fourniture.getImgUrl());
            });

            decline.setOnClickListener(view ->{
                dialog.dismiss();
                dialog.cancel();
            });

            dialog.show();


        }else {

          List <Acquired_Furnitures> acquired_furnitures = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(player.getId());

          for(Acquired_Furnitures furn : acquired_furnitures) {
              if(furn.getFurnitureType().equals(fourniture.getFurnitureType()))
              furn.setAvailable("false");
              MainMenu.myAppDataBase.myDao().updateAcquired_Furnitures(furn);
          }

            Toast.makeText(getApplicationContext(), fourniture.getName() + " is now used", Toast.LENGTH_SHORT).show();

            acquired_furnitures1.setAvailable("true");

            MainMenu.myAppDataBase.myDao().updateAcquired_Furnitures(acquired_furnitures1);

            //viewmodel.getAcquired_furn().getValue().add(acquired_furnitures1);

        }
    }

    @Override
    public void deliverSleep(int hoursNumber) {

        sleepTime=hoursNumber;


        energyBar.setProgress(hoursNumber * Params.ENERGY_GAIN_PER_HOUR + energyBar.getProgress());
        energypr.setText(energyBar.getProgress()+"/"+energyBar.getMax());

        hungerBar.setProgress(hungerBar.getProgress()-hoursNumber * Params.HUNGER_LOSS_PER_HOUR_IN_SLEEP);
        hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());

        if(hungerBar.getProgress()==0){
            healthbar.setProgress(healthbar.getProgress() - hoursNumber * Params.HEALTH_LOSS_PER_HOUR_IN_SLEEP);
            healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
        }

        hour+=sleepTime;

        if(hour>=24) {
            hour -= 24;
            day++;
            player.setBank_deposit(player.getBank_deposit()*1.01f);
            saveProgress();
            if(player.getStore_income() !=0) {

                player.setBalance(player.getBalance() + player.getStore_income());
                  balance.setText(player.getBalance() + "$");
            }
        }
        switcher=findViewById(R.id.switcher);

        switcher.animate().alpha(0f).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                switcher.animate().setDuration(250).alpha(1.0f);
                switcher.showNext();
            }
        });

            CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                @Override public void onTick(long millisUntilFinished){
                }
                @Override public void onFinish() {

                switcher.animate().alpha(0f).setDuration(250).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        switcher.animate().alpha(1.0f).setDuration(250);
                        switcher.showPrevious();
                    }
                });
                }
            };
        countDownTimer.start();
    }

    @Override
    public void deliverFood(Food food){

        double newBalance =player.getBalance()-food.getPrice();
        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");
            hungerBar.setProgress(food.getBenefit()+hungerBar.getProgress());
            hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());
        }
        else{

            showCustomToast("Insufficient funds to purchase "+food.getName(),food.getImgUrl(),"red");
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
            showCustomToast("Insufficient funds to purchase"+medicine.getName(),medicine.getImgUrl(),"red");
    }

    @Override
    public void deliverHouse(House house) {
        float newBalance = (int)player.getBalance()-house.getPrice();

        if(newBalance>=0){
            player.setBalance(newBalance);
            balance.setText(player.getBalance()+"$");
            showCustomToast("Congratulation you've bought "+house.getName(),house.getImagePath(),"green");
        }else
            showCustomToast("insuficient funds to buy "+house.getName(),house.getImagePath(),"red");
    }

    public  double getIncomeFromStore(){

        List<Acquired_Stores> acquired_stores =MainMenu.myAppDataBase.myDao().getAcquiredStores(player.getId());
        double income =0;

        if(!acquired_stores.isEmpty()){
            for (Acquired_Stores acq : acquired_stores){
                income += MainMenu.myAppDataBase.myDao().getStoreIncome(acq.getStore_id());
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
            player.setBalance(newBalance);
            balance.setText(player.getBalance()+"$");

            confirm.setOnClickListener(view ->{

                Acquired_Stores acquired_stores = new Acquired_Stores();
                acquired_stores.setStore_id(store.getId());
                acquired_stores.setPlayer_id(player.getId());
                dialog.dismiss();
                dialog.cancel();

                MainMenu.myAppDataBase.myDao().addAcquired_Store(acquired_stores);

                player.setStore_income(getIncomeFromStore());
                showCustomToast("Congratulation you purchased "+store.getName()+" !",store.getImgUrl(),"green");

                insertStoreFragment();

            });
            decline.setOnClickListener(view ->{
                dialog.dismiss();
                dialog.cancel();
            });

            dialog.show();

        }else {
            showCustomToast("Insufficient funds to purchase "+store.getName(),store.getImgUrl(),"red");
            dialog.dismiss();
            dialog.cancel();
        }
    }

    @Override
    public void onHomeShow(String url) {
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


        Acquired_degree acq =MainMenu.myAppDataBase.myDao().getAcqDegr(player.getId(),degree.getId());

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
            MainMenu.myAppDataBase.myDao().addAcquired_degree(acquired_degree);

            learning=true;

        }else
            {
                showCustomToast("insufficient funds to purchase "+degree.getName(),"","red");
            }
        }
            else {

            if(newBalance >=0) {
                acq.setPlayer_progress(acq.getPlayer_progress() + 5);
                player.setBalance(newBalance);
                balance.setText(player.getBalance() + "$");
                learning=true;
            }
            else
                showCustomToast("insufficient funds to purchase "+degree.getName(),"","red");

            if (!acq.getAvailable().equals("true"))
            {

                if (degree.getProgress() <= acq.getPlayer_progress()) {

                    acq.setAvailable("true");
                    showCustomToast("purchase of "+degree.getName()+" done succesfully","","green");
                }

                MainMenu.myAppDataBase.myDao().update_Acquired_Degree(acq);
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

        fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction=fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);

        Bundle bundle = new Bundle();

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
        mRewardVideoAd.loadAd(AD_VIDEO_ID, new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

        if(!player.getWork().equals(getString(R.string.none)))
            doubleEarn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {


        loadRewardAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        doubleEarnThread();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardAd();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    public void loadProgress(){
        database.Player loaded_player = MainMenu.myAppDataBase.myDao().getPlayer(slot);
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

        double workincome = MainMenu.myAppDataBase.myDao().work_incorme(player.getWork());

        player.setWork_income(workincome);

        MainMenu.myAppDataBase.myDao().updatePlayer(player);


        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Games.getLeaderboardsClient(this, account).submitScore(getString(R.string.leaderboard_score),
                    (long) player.getBalance());



        }

    }
}
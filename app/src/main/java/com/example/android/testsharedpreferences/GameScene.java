package com.example.android.testsharedpreferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import beans.Food;
import beans.Fourniture;
import beans.House;
import beans.Level;
import beans.Medicine;
import beans.Player;
import beans.Store;
import beans.Work;
import fragments.BankFragment;
import fragments.BuyFragment;
import fragments.FoodFragment;
import fragments.FournitureFragment;
import fragments.HouseFragment;
import fragments.IntroFragment;
import fragments.PharmacyFragment;
import fragments.SleepFragment;
import fragments.StoreFragment;
import fragments.WorkFragment;


public class GameScene extends AppCompatActivity
        implements WorkFragment.onWorkSelected, BuyFragment.OnBuyClicked,FournitureFragment.OnFournitureClicked
    ,SleepFragment.onSleepClicked,FoodFragment.onFoodClicked, PharmacyFragment.OnMedicineClicked,
        HouseFragment.OnHouseClicked, StoreFragment.OnStoreClicked,IntroFragment.OnStartWork
{


    private Button buy;
    private Button work;
    private Button sleep;
    private Button bank;
    private Button study;

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

    private TextView workType;
    private TextView income;
    private TextView balance;
    private Button  startWorking;


    private TextView intro;

    private  int minute ;
    private  int hour ;
    private  int day;
    private  int totalTime;

    private int sleepTime;

    private final static int TIME_SPEED_NORMAL =500;
    private final static int TIME_SPEED_FAST =250;
    private final static int TIME_SPEED_ULTRA_FAST =125;
    private final static int TIME_SPEED_SUPER_FAST =75;
    private int speed = TIME_SPEED_NORMAL;

    private SeekBar speedSeekBar;
    private TextView speedName;


    private WorkFragment workFragment ;
    private BankFragment bankFragment;
    private BuyFragment  buyFragment;

    private IntroFragment introFragment;

    private ConstraintLayout constraintLayout;
    private  Player player = new Player();
    private Work choosenWork;



    private LinearLayout linesButtons ;
    private ViewSwitcher switcher ;

    //for payment per hour
    private  int remainingMinutes;

    private int slot ;


    private boolean ignore=true;


    private static final  int XP_GAIN=25;

    private static final  int ENERGY_LOSS=8;

    private static final int ENERGY_GAIN_PER_HOUR=8;
    private static final int HUNGER_LOSS_PER_HOUR=4;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }


    public void runClockThread(){

        final Thread thread =new Thread(new Runnable(){


            @Override
            public void run(){

                while (threadRun) {

                    try {

                        Thread.sleep(speed);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                //For GAME OVER
                                if(healthbar.getProgress()<=0){
                                    threadRun=false;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GameScene.this);

                                    builder.setTitle("Game Over")
                                            .setMessage("you died cuz your health is ZeRo , good luck next Time !");
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                            clearPref();
                                            finish();
                                            dialog.dismiss();
                                        }
                                    });

                                    alertDialog.show();

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
                                    hungerBar.setProgress(hungerBar.getProgress()-HUNGER_LOSS_PER_HOUR);
                                    hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());

                                    if(hungerBar.getProgress()==0){
                                        healthbar.setProgress(healthbar.getProgress() -HUNGER_LOSS_PER_HOUR);
                                        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
                                    }

                                }
                                if (hour >= 24) {
                                    hour = 0;
                                    minute = 0;
                                    day++;
                                }
                                time.setText(hourS + ":" + minuteS);
                                dayView.setText(getString(R.string.day)+" "+ dayS);



                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        time=findViewById(R.id.time);
        dayView=findViewById(R.id.dayView);

         runClockThread();


         introFragment=new IntroFragment();
         fragmentInsertionSecond(introFragment);


        slot =getIntent().getIntExtra("slotNumber",0);



            healthbar=findViewById(R.id.healthBar);
            energyBar=findViewById(R.id.energyBar);
            hungerBar=findViewById(R.id.hungerBar);

            healthpr=findViewById(R.id.healthpr);
            energypr=findViewById(R.id.energypr);
            hungerpr=findViewById(R.id.hungerpr);

        caracterImg  = findViewById(R.id.caracterImg);
        jobName=findViewById(R.id.jobName);
        loadProgress();

            levelBar=findViewById(R.id.levelProgressBar);

            levelNumber=findViewById(R.id.levelNumber);

        work=findViewById(R.id.work);
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workFragment=new WorkFragment();
                Bundle bundle =new Bundle();
                bundle.putInt("playerLevel",player.getLevel().getLevel());

                workFragment.setArguments(bundle);
                fragmentInsertion(workFragment);

            }
        });

        bank=findViewById(R.id.bank);
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankFragment=new BankFragment();
                fragmentInsertion(bankFragment);

            }
        });

        buy=(Button)findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyFragment =new BuyFragment();
                fragmentInsertion(buyFragment);

            }
        });

        sleep=findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SleepFragment sleepFragment =new SleepFragment();
                fragmentInsertion(sleepFragment);
            }
        });

        study=findViewById(R.id.study);

        playerN = (TextView)findViewById(R.id.playerN);
        playerN.setText(player.getName());

        balance=findViewById(R.id.balance);
        balance.setText(player.getBalance()+"$");

        workType=findViewById(R.id.workType);
        workType.setText(player.getWork().getName());

        income=findViewById(R.id.income);
        income.setText(player.getWork().getPay()+"$/"+getString(R.string.day));




        constraintLayout =findViewById(R.id.introLayout);

        //Init of progress bars
        initialiseProgressBars();

        // when the button start work is pressed
        startWorking=findViewById(R.id.startWorking);

        if(player.getWork().getName().equals("none"))
            startWorking.setEnabled(false);
        else
            startWorking.setEnabled(true);

        startWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startWorkThread();
            }
        });



        speedSeekBar=(SeekBar)findViewById(R.id.speedSeekBar);
        speedName=findViewById(R.id.speedName);


        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if(progress==0){
                    speedName.setText(getString(R.string.normalSpeed));
                    speed=TIME_SPEED_NORMAL;
                };
                if(progress==1){
                    speedName.setText(getString(R.string.fastSpeed));
                    speed=TIME_SPEED_FAST;
                };
                if(progress==2){
                    speedName.setText(getString(R.string.ultraSpeed));
                    speed=TIME_SPEED_ULTRA_FAST;
                };
                if(progress==3){
                    speedName.setText(getString(R.string.superSpeed));
                    speed=TIME_SPEED_SUPER_FAST;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        linesButtons=findViewById(R.id.buttonsLine);




    }
    public void startWorkThread(){

        final Handler handler =new Handler(Looper.getMainLooper());
        Thread thread =new Thread(new Runnable() {

            @Override
            public void run() {

                 remainingMinutes =player.getWorkMinutes();
                while (remainingMinutes>0) {

                    balance = findViewById(R.id.balance);

                    try {




                        Thread.sleep(speed);

                        handler.post(new Runnable(){

                            @Override
                            public void run() {
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

                                introFragment.setTextView(remainingMinutes/60);

                                ignore=false;
                                if(remainingMinutes % 60 == 0) {




                                    //increment player balance with work pay
                                    player.setBalance(player.getWork().getPay() + player.getBalance());

                                    balance.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.money_gain);
                                            mp.start();
                                            balance.setText(player.getBalance() + "$");
                                            balance.animate().scaleX(1f).scaleY(1f).setDuration(300);
                                        }
                                    });



                                    //reduce player energy
                                    energyBar.setProgress(energyBar.getProgress()-ENERGY_LOSS);
                                    energypr.setText(energyBar.getProgress()+"/"+energyBar.getMax());

                                    //increment player progress for a level
                                    player.getLevel().setProgressLevel(player.getLevel().getProgressLevel()+XP_GAIN);
                                    levelBar.setProgress(player.getLevel().getProgressLevel());
                                    //progressLevel.setText(player.getLevel().getProgressLevel()+"/"+player.getLevel().getMaxProgress()+getString(R.string.xp));
                                }

                                if(player.getLevel().getMaxProgress()<=player.getLevel().getProgressLevel()) {

                                    //when the player level_up !
                                    int actualProgress =player.getLevel().getProgressLevel()-player.getLevel().getMaxProgress();
                                    player.upgradeLevel();

                                    levelNumber.animate().scaleX(1.3f).scaleY(1.3f).setDuration(300).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {


                                            MediaPlayer mp =MediaPlayer.create(getApplicationContext(),R.raw.level_up);

                                            if(player.getLevel().getLevel()<10)
                                                levelNumber.setText(getString(R.string.level) +": 0"+ player.getLevel().getLevel());
                                            else
                                                levelNumber.setText(getString(R.string.level) +": "+ player.getLevel().getLevel());


                                            levelNumber.animate().scaleX(1f).scaleY(1f).setDuration(300);
                                            mp.start();
                                        }
                                    });

                                    levelBar.setMax(player.getLevel().getMaxProgress());
                                    levelBar.setProgress(player.getLevel().getProgressLevel()+actualProgress);
                                    //progressLevel.setText(levelBar.getProgress()+"/"+player.getLevel().getMaxProgress()+getString(R.string.xp));

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
                                    speed=TIME_SPEED_NORMAL;

                                    ignore=true;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    remainingMinutes-=1;
                }
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

        levelBar.setMax(player.getLevel().getMaxProgress());
        levelBar.setProgress(player.getLevel().getProgressLevel());

        if(player.getLevel().getLevel()<10)
            levelNumber.setText(getString(R.string.level)+": 0"+player.getLevel().getLevel());

        else
            levelNumber.setText(getString(R.string.level)+": "+player.getLevel().getLevel());

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



    //To execute tasks from fragments
    @Override
    public void deliverWork(Work work) {


        workType=findViewById(R.id.workType);
        workType.setText(work.getName());
        income=findViewById(R.id.income);
        income.setText(work.getPay()+"$/Day");

        choosenWork =new Work(
                 work.getName()
                ,work.getPay()
                ,work.getLeveltoWork()
                ,work.getTimeOfWork()
                ,work.getImagePath());

        player.setWork(choosenWork);
        player.setWorkMinutes(choosenWork.getTimeOfWork()*60);
        Uri imgURI = Uri.parse(work.getImagePath());
        caracterImg.setImageURI(imgURI);
            saveProgress();
        startWorking.setEnabled(true);
        jobName.setText(work.getName());


    }

    @Override
    public void deliverBuy(String nameOfFragment) {


        if(nameOfFragment.equals("Food")){

        FoodFragment foodFragment =new FoodFragment();
        fragmentInsertionSecond(foodFragment);

        }
        if(nameOfFragment.equals("Fourniture")) {

            FournitureFragment fournitureFragment =new FournitureFragment();
            fragmentInsertionSecond(fournitureFragment);

        }
        if(nameOfFragment.equals("House")) {

            HouseFragment houseFragment=new HouseFragment();
            fragmentInsertionSecond(houseFragment);
        }

        if(nameOfFragment.equals("Store")){
            StoreFragment storeFragment =new StoreFragment();
            fragmentInsertionSecond(storeFragment);
        }

        if(nameOfFragment.equals("Pharmacy")){
            PharmacyFragment pharmacyFragment = new PharmacyFragment();
            fragmentInsertionSecond(pharmacyFragment);
        }
    }

    @Override
    public void deliverFourniture(final Fourniture fourniture) {

        AlertDialog.Builder builder =new AlertDialog.Builder(GameScene.this);

        builder.setTitle("Purchase")
                .setMessage("Would you like to purchase this item ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        float newBalance=player.getBalance()-fourniture.getPrice();

                        String message;
                        if(newBalance>=0) {
                            player.setBalance(newBalance);
                            balance.setText(newBalance + "$");
                            message=" you purchased a "+fourniture.getName()+" for "+fourniture.getPrice()+"$ !";
                        }else{
                            message =" insufficiant funds to purchase "+fourniture.getName();
                        }
                        showPurchaseDialog(message);

                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog =builder.create();
        alertDialog.show();

    }

    @Override
    public void deliverSleep(int hoursNumber) {

        sleepTime=hoursNumber;


        energyBar.setProgress(hoursNumber * ENERGY_GAIN_PER_HOUR + energyBar.getProgress());
        energypr.setText(energyBar.getProgress()+"/"+energyBar.getMax());

        hungerBar.setProgress(hungerBar.getProgress()-hoursNumber * (HUNGER_LOSS_PER_HOUR/3));
        hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());

        if(hungerBar.getProgress()==0){
            healthbar.setProgress(healthbar.getProgress() - hoursNumber * (HUNGER_LOSS_PER_HOUR/3));
            healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());
        }

        hour+=sleepTime;

        if(hour>=24) {
            hour -= 24;
            day++;
        }

        switcher=findViewById(R.id.switcher);


            switcher.showNext();

            CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                @Override public void onTick(long millisUntilFinished) {
                }
                @Override public void onFinish() {


             switcher.showPrevious();
                }
            };
        countDownTimer.start();
    }

    @Override
    public void deliverFood(Food food){

        float newBalance =player.getBalance()-food.getPrice();
        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");
            hungerBar.setProgress(food.getBenefit()+hungerBar.getProgress());
            hungerpr.setText(hungerBar.getProgress()+"/"+hungerBar.getMax());
        }
        else{
            Toast.makeText(getApplicationContext(),"Insufficient funds to purchase "+food.getName(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void deliverMedicine(Medicine medicine) {

        healthbar.setProgress(healthbar.getProgress()+medicine.getBenefit());
        healthpr.setText(healthbar.getProgress()+"/"+healthbar.getMax());

        float newBalance=player.getBalance()-medicine.getPrice();
        if(newBalance>=0) {
            player.setBalance(newBalance);
            balance.setText(player.getBalance() + "$");
        }else
            Toast.makeText(getApplicationContext(),"Insufficient funds to purchase"+medicine.getName(),Toast.LENGTH_SHORT).show();


    }

    @Override
    public void deliverHouse(House house) {
        float newBalance = player.getBalance()-house.getPrice();

        if(newBalance>=0){
            player.setBalance(newBalance);
            balance.setText(player.getBalance()+"$");
        }else
            Toast.makeText(getApplicationContext(),"insuficient funds to buy "+house.getName(),Toast.LENGTH_SHORT).show();
    }


    @Override
    public void deliverStore(Store store) {

        float newBalance = player.getBalance()-store.getPrice();

        if(newBalance>=0){
            player.setBalance(newBalance);
            balance.setText(player.getBalance()+"$");
            Toast.makeText(getApplicationContext(),"congratulation you purchased"+store.getName()+" !",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"insufficient funds to purchase "+store.getName(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void startDecHour() {

    }

    public void showPurchaseDialog(String msg){

        AlertDialog.Builder builder=new AlertDialog.Builder(GameScene.this);

        builder.setTitle("Purchase")
                .setMessage(msg);

        AlertDialog alertDialog=builder.create();

        alertDialog.show();

    }

    public void saveProgress(){


        if(slot==1)
        sharedPreferences=getSharedPreferences(getString(R.string.prefSlot1),Context.MODE_PRIVATE);
        if(slot==2)
        sharedPreferences=getSharedPreferences(getString(R.string.prefSlot2),Context.MODE_PRIVATE);
        if(slot==3)
        sharedPreferences=getSharedPreferences(getString(R.string.prefSlot3),Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        //save player things
        editor.putFloat("balance",player.getBalance());
        editor.putString("work",player.getWork().getName());
        editor.putFloat("pay",player.getWork().getPay());

        if(choosenWork!=null)
        editor.putString("imagePath",choosenWork.getImagePath());

        editor.putInt("workTimeMinute",player.getWorkMinutes());
        editor.putInt("level",player.getLevel().getLevel());
        editor.putInt("maxProgress",player.getLevel().getMaxProgress());
        editor.putInt("progressLevel",player.getLevel().getProgressLevel());
        //save time
        editor.putInt("day", day);
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.putInt("totalTime", totalTime);

        //saveProgressBars
        editor.putInt("healthBar",healthbar.getProgress());
        editor.putInt("energyBar",energyBar.getProgress());
        editor.putInt("hungerBar",hungerBar.getProgress());

        editor.apply();

    }

    public void loadProgress(){

        if(slot==1)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot1),Context.MODE_PRIVATE);
        if(slot==2)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot2),Context.MODE_PRIVATE);
        if(slot==3)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot3),Context.MODE_PRIVATE);


        //load player data
        player.setName(sharedPreferences.getString("PlayerName","none"));
        player.setBalance(sharedPreferences.getFloat("balance",0));
        player.getWork().setName(sharedPreferences.getString("work","none"));
        player.getWork().setPay(sharedPreferences.getFloat("pay",0));
        player.setWorkMinutes(sharedPreferences.getInt("workTimeMinute",0));



        Uri imgURI = Uri.parse(sharedPreferences.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty"));
        caracterImg.setImageURI(imgURI);
        jobName.setText(player.getWork().getName());


        Level level =new Level();
        level.setLevel(sharedPreferences.getInt("level",0));
        level.setMaxProgress(sharedPreferences.getInt("maxProgress",100));
        level.setProgressLevel(sharedPreferences.getInt("progressLevel",0));

        player.setLevel(level);
        //load time of game
        minute= sharedPreferences.getInt("minute",0);
        hour=sharedPreferences.getInt("hour",0);
        day=sharedPreferences.getInt("day",0);

        totalTime=(day*24)+(hour*60)+minute;

        //load progressbars
        healthbar.setProgress(sharedPreferences.getInt("healthBar",100));
        energyBar.setProgress(sharedPreferences.getInt("energyBar",100));
        hungerBar.setProgress(sharedPreferences.getInt("hungerBar",100));

    }

    public void fragmentInsertion(Fragment fragment){

        fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placefragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }

    public void fragmentInsertionSecond(Fragment fragment){
        fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placefragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void clearPref(){
        if(slot==1)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot1),Context.MODE_PRIVATE);
        if(slot==2)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot2),Context.MODE_PRIVATE);
        if(slot==3)
            sharedPreferences=getSharedPreferences(getString(R.string.prefSlot3),Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }




}
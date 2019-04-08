package com.houbenz.lifesimulator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import beans.Buy;
import beans.Food;
import beans.Furniture;
import beans.Learn;
import beans.Medicine;
import beans.Store;
import conf.Params;
import database.Car;
import database.Degree;
import database.Gift;
import database.MainFragments;
import database.MyAppDataBase;
import database.Player;
import database.VersionDB;

public class MainMenu extends AppCompatActivity {


    private Button newGame;
    private Button loadGame;
    private Button settings;
    private Button credits;

    public static MyAppDataBase myAppDataBase;


    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private int firsttime;

    private ConstraintLayout mainLayout;

    private SharedPreferences sharedPreferences;


    private static final int RC_SIGN_IN = 106 ;
    private static final int RC_LEADERBOARD_SCORE =1005;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener mOnTouchListener = (v, event) -> {

        //delayedHide();

        hideSystemUI();
        return false;
    };


    static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Acquired_Degree add column player_progress integer default 0 not null");
            database.execSQL("alter table Acquired_Degree add column available varchar");

            database.execSQL("alter table degree add column progress integer default 0 not null");

        }
    };

    static  final Migration MIGRATION_15_16 = new Migration(15,16) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Car(" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "price real," +
                    "imgUrl text)");
        }
    };

    static  final Migration MIGRATION_16_17 = new Migration(16,17) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Acquired_Cars(id INTEGER primary key autoincrement not null," +
                    "car_id INTEGER not null," +
                    "player_id INTEGER not null," +
                    "foreign key(player_id) references Player(id) on delete cascade on update cascade," +
                    "foreign key(car_id) references Car(id) on delete cascade on update cascade )");
        }
    };

    static  final Migration MIGRATION_17_18 = new Migration(17,18) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE Gift(" +
                    "id integer PRIMARY KEY AUTOINCREMENT not null," +
                    "name TEXT," +
                    "price REAL not null," +
                    "imgUrl TEXT)");
        }
    };

    static  final Migration MIGRATION_18_19 = new Migration(18,19) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Acquired_Gifts(id INTEGER primary key autoincrement not null," +
                    "gift_id INTEGER not null," +
                    "player_id INTEGER not null," +
                    "foreign key(player_id) references Player(id) on delete cascade on update cascade," +
                    "foreign key(gift_id) references Gift(id) on delete cascade on update cascade )");
        }
    };

    static  final Migration MIGRATION_19_20 = new Migration(19,20) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("drop table Acquired_Gifts");
            database.execSQL("alter table Gift add column giftCount INTEGER not null default 0");
            database.execSQL("alter table Player add column dating TEXT");

        }
    };

    static  final Migration MIGRATION_20_21 = new Migration(20,21) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Player add column dating TEXT");
        }
    };

    static  final Migration MIGRATION_21_22 = new Migration(21,22) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Player add column relationBar INTEGER not null default 0");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        try {
            myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "life_simulatordb")
                    .addMigrations(MIGRATION_14_15,MIGRATION_15_16,MIGRATION_16_17,
                            MIGRATION_17_18,MIGRATION_18_19,MIGRATION_19_20,MIGRATION_20_21,MIGRATION_21_22)
                    .allowMainThreadQueries().build();

        } catch (IllegalStateException e) {

            myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "life_simulatordb")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("myShared", Context.MODE_PRIVATE);


        String entry = sharedPreferences.getString("entry", "none");


        if (entry.equals("none")) {
            initGifts(false);
            initWorkRows(false);
            initDegreeRows(false);
            initFragments(false);
            initFood(false);
            initStores(false);
            initFurnitures(false);
            initMedicine(false);
            initCars(false);
            myAppDataBase.myDao().initDBVersion(new VersionDB(getDatabaseVersion(), 1));

            sharedPreferences.edit().putString("entry", "available").apply();
        } else {

            String actualversion = getDatabaseVersion();
            VersionDB versionDB = myAppDataBase.myDao().getVersionDB();

            if (versionDB == null) {
                myAppDataBase.myDao().initDBVersion(new VersionDB(getDatabaseVersion(), 1));
                versionDB = myAppDataBase.myDao().getVersionDB();
            }


            if (!versionDB.getVersion().equals(actualversion)) {

                initGifts(true);
                initWorkRows(true);
                initDegreeRows(true);
                initFragments(true);
                initFood(true);
                initStores(true);
                initFurnitures(true);
                initMedicine(true);
                initCars(true);
                versionDB.setVersion(actualversion);
                myAppDataBase.myDao().updateVerionDB(versionDB);
            }

        }


        newGame = findViewById(R.id.newgame);
        loadGame = findViewById(R.id.loadgame);
        settings = findViewById(R.id.settings);
        credits = findViewById(R.id.credits);


        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);


        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(mOnTouchListener);


        credits.setOnClickListener(view -> {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if(account != null)
            Games.getLeaderboardsClient(this,account).getLeaderboardIntent(getString(R.string.leaderboard_score))
                    .addOnSuccessListener(intent -> {
                        startActivityForResult(intent,RC_LEADERBOARD_SCORE);
                    });

        });


        newGame.setOnClickListener(view -> {

            dialogueCreatorNewGame();
        });

        loadGame.setOnClickListener(view -> {

            dialogueCreatorLoadGame();

        });

        animateButton();


        firsttime = 4000;

        Thread thread = new Thread(() -> {

            while (!isDestroyed()) {

                try {


                    Thread.sleep(firsttime);
                    firsttime = 1500;
                    runOnUiThread(() -> animateInThread(imageView3));

                    Thread.sleep(1500);
                    runOnUiThread(() -> animateInThread(imageView4));

                    Thread.sleep(2000);
                    runOnUiThread(() -> animateInThread(imageView1));

                    Thread.sleep(1500);
                    runOnUiThread(() -> animateInThread(imageView2));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        thread.start();


        startSignInIntent();


    }

    private void startSignInIntent(){

        GoogleSignInClient signInClient=GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        Intent intent=signInClient.getSignInIntent();

        startActivityForResult(intent,RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                Toast.makeText(getApplicationContext(),"Auth Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Auth Failed",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void animateInThread(ImageView imageView) {
        int randomNumber = (int) ((imagesResources().length - 0) * Math.random());

        animateImagesAlphaToZero(imageView, 4000);
        imageView.setImageResource(imagesResources()[randomNumber]);
        animateImageAlphaToOne(imageView, 4000);

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

    //Affiche les slots pour charger une partie

    public void dialogueCreatorLoadGame() {

        Player player = myAppDataBase.myDao().getPlayer(1);

        if(player != null) {
            Intent intent = new Intent(MainMenu.this, GameScene.class);
            intent.putExtra("slotNumber", 1);
            startActivity(intent);
        }
    }

    //Affiche les slots pour un nouveau joueur
    public void dialogueCreatorNewGame() {

        Player player =myAppDataBase.myDao().getPlayer(1);

        if(player == null){
            dialogInputCreate( 1);
        }
        else
        {
            overwriteSlotDilaog(1);
        }

    }

    public void deselectButtons(){
        newGame.setSelected(false);
        loadGame.setSelected(false);
        credits.setSelected(false);
    }

    //Pour entrer le nom du joueur puis sauvgarder la premiere session
    public void dialogInputCreate(final int slotNumber){

        final Dialog putText = new Dialog(MainMenu.this);

        putText.setTitle("put your name");

        putText.setContentView(R.layout.caracter_name);

        final TextInputLayout caracterName = putText.findViewById(R.id.nameInput);

        Button closeDialog =putText.findViewById(R.id.closeDialog);

        closeDialog.setOnClickListener(view -> {

                putText.dismiss();
        });

        Button confirmDialog =putText.findViewById(R.id.confirm);

        confirmDialog.setOnClickListener(view -> {


                if(caracterName.getEditText().getText().toString().equals("")) {
                    caracterName.setError("you must write at least 1 character");
                }
                else
                {

                    Player player = new Player();
                    player.setName(caracterName.getEditText().getText().toString());
                    Calendar cal = Calendar.getInstance();
                    String creationDate =cal.get(Calendar.DATE) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR)
                            + " at " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
                    player.setCreation_date(creationDate);

                    player.setId(slotNumber);

                    player.setWork_image_path("android.resource://com.houbenz.android.lifesimulator/drawable/ic_empty");
                    player.setWork(getString(R.string.none));
                    player.setBalance(Params.START_BALANCE);
                    player.setBank_deposit(0);
                    player.setWork_time(0);
                    player.setWork_income(0);
                    player.setDating("false");

                    player.setMax_progress(100);
                    player.setHealthbar(Params.HEALTH_VALUE);
                    player.setEnergybar(Params.ENERGY_VALUE);
                    player.setHungerbar(Params.HUNGER_VALUE);

                    myAppDataBase.myDao().addPlayer(player);

                    putText.dismiss();

                    Intent intent = new Intent(MainMenu.this, GameScene.class);
                    intent.putExtra("slotNumber", slotNumber);
                     startActivity(intent);

                }
        });

        putText.show();
    }


    //pour ecraser une partie lorsque le joueur utilise une slot deja prise
    public void overwriteSlotDilaog(final int slotNumber) {

        final AlertDialog.Builder overwriteDialog = new AlertDialog.Builder(MainMenu.this);


        overwriteDialog.setMessage(getString(R.string.overwriteTitle))
                .setPositiveButton(getString(R.string.overwrite), (dialog, which) -> {

                    myAppDataBase.myDao().deletePlayer(myAppDataBase.myDao().getPlayer(slotNumber));
                    List<Gift> gifts = myAppDataBase.myDao().getGifts();

                    //
                    for(Gift gift : gifts){
                        gift.setGiftCount(0);
                       myAppDataBase.myDao().updateGift(gift);
                    }

                    dialogInputCreate(slotNumber);

                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                    dialog.cancel();
                    dialog.dismiss();
                });

        AlertDialog alertDialog = overwriteDialog.create();

        alertDialog.show();

    }

    public void animateButton(){

        animateView(newGame,1500);
        animateView(loadGame,2000);
        animateView(settings,2500);
        animateView(credits,3000);

        animateTranslationImageX(imageView1,1500);
        animateTranslationImageX(imageView2,2000);
        animateTranslationImageX(imageView3,2500);
        animateTranslationImageX(imageView4,3000);
    }



    public void animateView(View view, int duration){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,View.TRANSLATION_Y,-1000f,0f);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }


    public void animateImagesAlphaToZero(ImageView imageView, int duration){
        ObjectAnimator objectAnimator =ObjectAnimator.ofFloat(imageView, View.ALPHA,1f,0f);
        objectAnimator.setDuration(duration);
        objectAnimator.start();

    }


    public void animateTranslationImageX(ImageView imageView, int duration){
        ObjectAnimator objectAnimator1 =ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X,-1000f,0f);
        objectAnimator1.setDuration(duration);
        objectAnimator1.start();
    }

    public void animateImageAlphaToOne(ImageView view , int duration){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,View.ALPHA,0f,1f);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }



    public int[] imagesResources(){

        int[] arrayImage =new int[13];
        arrayImage[0]=R.drawable.ic_empty;
        arrayImage[1]=R.drawable.ic_firefighter;
        arrayImage[2]=R.drawable.ic_waiter;
        arrayImage[3]=R.drawable.ic_deliveryman;
        arrayImage[4]=R.drawable.ic_programmer;
        arrayImage[5]=R.drawable.ic_teacher;
        arrayImage[6]=R.drawable.ic_noodles;
        arrayImage[7]=R.drawable.ic_professor;
        arrayImage[8]=R.drawable.ic_food;
        arrayImage[9]=R.drawable.ic_pilot;
        arrayImage[10]=R.drawable.syrup;
        arrayImage[11]=R.drawable.grass;
        arrayImage[12]=R.drawable.pills;
        return arrayImage;
    }


    public void initWorkRows(boolean isUpdate){
       ArrayList<beans.Work> worksBeans = beans.Work.workInit(getApplicationContext()) ;

       for(beans.Work bean : worksBeans){
           database.Work work = new database.Work();

           work.setId(bean.getId());

           work.setDegree_required(bean.getReqDegree());
           work.setImgPath(bean.getImagePath());
           work.setIncome(bean.getPay());
           work.setLvlToWork(bean.getLeveltoWork());
           work.setName(bean.getName());
           work.setWork_time(bean.getTimeOfWork());

           if(!isUpdate)
                myAppDataBase.myDao().addWork(work);
           else
               myAppDataBase.myDao().updateWork(work);
       }

       int oldRows =myAppDataBase.myDao().workNumber();

       int newRows = worksBeans.size() - oldRows;

       if(newRows >0) {
           for(int i =oldRows  ; i<worksBeans.size();i++){

               database.Work work = new database.Work();

               work.setId(worksBeans.get(i).getId());

               work.setDegree_required(worksBeans.get(i).getReqDegree());
               work.setImgPath(worksBeans.get(i).getImagePath());
               work.setIncome(worksBeans.get(i).getPay());
               work.setLvlToWork(worksBeans.get(i).getLeveltoWork());
               work.setName(worksBeans.get(i).getName());
               work.setWork_time(worksBeans.get(i).getTimeOfWork());

               myAppDataBase.myDao().addWork(work);
           }
       }


    }

    public void initDegreeRows(boolean isUpdate){
        ArrayList<Learn> learns = Learn.initLearn(getApplicationContext());

        for(Learn learn : learns){
            Degree degree = new Degree();

            degree.setId(learn.getId());
            degree.setName(learn.getName());
            degree.setPrice(learn.getPrice());

            degree.setProgress(learn.getProgress());

            if(!isUpdate)
                myAppDataBase.myDao().addDegree(degree);
            else
                myAppDataBase.myDao().updateDegree(degree);
        }

        int oldRows =myAppDataBase.myDao().degreeNumber();

        int newRows =learns.size() - oldRows;
        if (newRows > 0){
            for (int i = oldRows;i<learns.size();i++){

                Degree degree = new Degree();

                degree.setId(learns.get(i).getId());
                degree.setName(learns.get(i).getName());
                degree.setPrice(learns.get(i).getPrice());

                degree.setProgress(learns.get(i).getProgress());

                myAppDataBase.myDao().addDegree(degree);
            }
        }
    }


    public void initFood(boolean isUpdate){
        ArrayList<Food>  foods= Food.initFood(getApplicationContext());

        for(Food food : foods){
            database.Food foodDb = new database.Food();

            foodDb.setId(food.getId());
            foodDb.setName(food.getName());
            foodDb.setPrice(food.getPrice());
            foodDb.setBenefit(food.getBenefit());
            foodDb.setDescription(food.getDescription());
            foodDb.setImgUrl(food.getImagePath());
            if(!isUpdate)
                myAppDataBase.myDao().addFood(foodDb);
            else
                myAppDataBase.myDao().updateFood(foodDb);
        }

        int oldRows =myAppDataBase.myDao().foodNumber();

        int newRows = foods.size()-oldRows;

        if (newRows > 0){
            for(int i = oldRows;i<foods.size();i++){

                database.Food foodDb = new database.Food();

                foodDb.setName(foods.get(i).getName());
                foodDb.setPrice(foods.get(i).getPrice());
                foodDb.setBenefit(foods.get(i).getBenefit());
                foodDb.setDescription(foods.get(i).getDescription());
                foodDb.setImgUrl(foods.get(i).getImagePath());

                myAppDataBase.myDao().addFood(foodDb);
            }

        }


    }

    public void initMedicine(boolean isUpdate){
        ArrayList<Medicine> medicines = Medicine.initMedicine(getApplicationContext());


        for(Medicine medicine : medicines) {
            database.Medicine medicineDb = new database.Medicine();

            medicineDb.setId(medicine.getId());
            medicineDb.setName(medicine.getName());
            medicineDb.setPrice(medicine.getPrice());
            medicineDb.setBenefit(medicine.getBenefit());
            medicineDb.setImgUrl(medicine.getImagePath());
            if (!isUpdate)
                myAppDataBase.myDao().addMedicine(medicineDb);
            else
                myAppDataBase.myDao().updateMedicine(medicineDb);
        }
            int oldRows =myAppDataBase.myDao().medicineNumber();
            int newRows = medicines.size()-oldRows;

            if(newRows > 0){
                for (int i=oldRows;i <medicines.size();i++){

                    database.Medicine medicineDb = new database.Medicine();

                    medicineDb.setName(medicines.get(i).getName());
                    medicineDb.setPrice(medicines.get(i).getPrice());
                    medicineDb.setBenefit(medicines.get(i).getBenefit());
                    medicineDb.setImgUrl(medicines.get(i).getImagePath());

                    myAppDataBase.myDao().addMedicine(medicineDb);
                }
            }

    }

    public  void initFragments(boolean isUpdate){
        ArrayList<Buy> buys = Buy.initBuy(getApplicationContext());


        for (Buy buy : buys){
            MainFragments mainFragments = new MainFragments();
            mainFragments.setId(buy.getId());
            mainFragments.setName(buy.getName());
            mainFragments.setColor(buy.getColor());
            mainFragments.setImage_Uri(buy.getImagePath());

            if(!isUpdate)
                myAppDataBase.myDao().addMainFragment(mainFragments);
            else
                myAppDataBase.myDao().updateMainFragment(mainFragments);
        }

        int oldRows = myAppDataBase.myDao().fragmentNumber();
        int newRows =buys.size()-oldRows;

        if(newRows > 0){

            for(int i=oldRows; i <buys.size();i++){

                MainFragments mainFragments = new MainFragments();
                mainFragments.setId(buys.get(i).getId());
                mainFragments.setName(buys.get(i).getName());
                mainFragments.setColor(buys.get(i).getColor());
                mainFragments.setImage_Uri(buys.get(i).getImagePath());

                myAppDataBase.myDao().addMainFragment(mainFragments);
            }
        }
    }

    public void initStores(boolean isUpdate){
        ArrayList<Store> stores = Store.initStore(getApplicationContext());

        for (Store store : stores) {
            database.Store storeDb = new database.Store();

            storeDb.setId(store.getId());
            storeDb.setName(store.getName());
            storeDb.setPrice(store.getPrice());
            storeDb.setImgUrl(store.getUri());
            storeDb.setIncome(store.getIncome());
            if (!isUpdate)
                myAppDataBase.myDao().addStore(storeDb);
            else
                myAppDataBase.myDao().updateStore(storeDb);


        }


            int oldRows =myAppDataBase.myDao().storeNumber();
            int newRows = stores.size() - oldRows;

            if(newRows > 0 ){
              for(int i = oldRows;i<stores.size(); i++){

                  database.Store storeDb = new database.Store();
                  storeDb.setName(stores.get(i).getName());
                  storeDb.setPrice(stores.get(i).getPrice());
                  storeDb.setImgUrl(stores.get(i).getUri());
                  storeDb.setIncome(stores.get(i).getIncome());

                  myAppDataBase.myDao().addStore(storeDb);
              }
            }




    }

    public  void initFurnitures(boolean isUpdate){
        ArrayList<Furniture> furnitures = Furniture.initFourniture(getApplicationContext());

        for (Furniture furniture : furnitures){
            database.Furniture furnitureDb = new database.Furniture();

            furnitureDb.setId(furniture.getId());
            furnitureDb.setName(furniture.getName());
            furnitureDb.setPrice(furniture.getPrice());
            furnitureDb.setImgUrl(furniture.getUrl());
            furnitureDb.setFurnitureType(furniture.getFournitureType());

            if(!isUpdate)
                myAppDataBase.myDao().addFurnitures(furnitureDb);
            else
                myAppDataBase.myDao().updateFurniture(furnitureDb);
        }

        int oldRows =myAppDataBase.myDao().furnitureNumber();
        int newRows = furnitures.size() - oldRows;

        if(newRows > 0) {
            for(int i =oldRows ; i<furnitures.size() ; i++){

                database.Furniture furnitureDb = new database.Furniture();
                furnitureDb.setName(furnitures.get(i).getName());
                furnitureDb.setPrice(furnitures.get(i).getPrice());
                furnitureDb.setImgUrl(furnitures.get(i).getUrl());
                furnitureDb.setFurnitureType(furnitures.get(i).getFournitureType());

                myAppDataBase.myDao().addFurnitures(furnitureDb);
            }
        }


    }

    public void initCars(boolean isUpdate){

        InputStream is ;
        String json ;
        try{
            is=getApplicationContext().getAssets().open("cars.json");

            int size =is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);


            for (int i =0 ; i<jsonArray.length();i++){
                Car car = new Car();
             JSONObject jsonObject = jsonArray.getJSONObject(i);
             car.setId(jsonObject.getInt("id"));
             car.setName(jsonObject.getString("name"));
             car.setPrice(jsonObject.getDouble("price"));
             car.setImgUrl(jsonObject.getString("uri"));

                if(!isUpdate)
                    MainMenu.myAppDataBase.myDao().addCar(car);
                else
                    MainMenu.myAppDataBase.myDao().updateCar(car);
            }

            int oldRows = MainMenu.myAppDataBase.myDao().carsNumber();
            int newRows = jsonArray.length() - oldRows;


            //for when we update the file with a new row

            if(newRows > 0)
                for(int i = oldRows ; i<jsonArray.length();i++){

                    Car car = new Car();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    car.setId(jsonObject.getInt("id"));
                    car.setName(jsonObject.getString("name"));
                    car.setPrice(jsonObject.getDouble("price"));
                    car.setImgUrl(jsonObject.getString("uri"));

                        MainMenu.myAppDataBase.myDao().updateCar(car);
                }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    public void initGifts(boolean isUpdate){

        InputStream is ;
        String json ;
        try {
            is = getApplicationContext().getAssets().open("gifts.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json=new String (buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);


            for(int i =0 ; i<jsonArray.length();i++){
                JSONObject jo=jsonArray.getJSONObject(i);
                Gift gift = new Gift();
                gift.setId(jo.getInt("id"));
                gift.setName(jo.getString("name"));
                gift.setImgUrl(jo.getString("uri"));
                gift.setPrice(jo.getInt("price"));
                gift.setGiftCount(jo.getInt("giftCount"));

                if(!isUpdate)
                    myAppDataBase.myDao().addGift(gift);
                else
                    myAppDataBase.myDao().updateGift(gift);
            }

            int oldRows = myAppDataBase.myDao().giftNumber();
            int newRows = jsonArray.length() - oldRows;

            if(newRows > 0){

                for(int i =oldRows ; i<jsonArray.length();i++){

                    JSONObject jo=jsonArray.getJSONObject(i);

                    Gift gift = new Gift();

                    gift.setId(jo.getInt("id"));
                    gift.setName(jo.getString("name"));
                    gift.setImgUrl(jo.getString("uri"));
                    gift.setPrice(jo.getInt("price"));
                    gift.setGiftCount(jo.getInt("giftCount"));

                    myAppDataBase.myDao().addGift(gift);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public String getDatabaseVersion(){

        InputStream is=null ;
        String db_version= "";
        String json ;

        try {
           is = getApplicationContext().getAssets().open("database-version.json");


            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json=new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);

             db_version =jsonObject.getString("version");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return db_version;
    }

}

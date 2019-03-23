package com.houbenz.lifesimulator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;

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
import database.Degree;
import database.MainFragments;
import database.MyAppDataBase;
import database.Player;
import database.VersionDB;

public class MainMenu extends AppCompatActivity{


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

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener mOnTouchListener = (v, event) -> {

        //delayedHide();

        hideSystemUI();
        return false;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        try {
            myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "life_simulatordb")
                    .allowMainThreadQueries().build();

        }catch (IllegalStateException e){

            myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "life_simulatordb")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        sharedPreferences= getApplicationContext().getSharedPreferences("myShared",Context.MODE_PRIVATE);


        String entry =sharedPreferences.getString("entry","none");


        if(entry.equals("none")){
            initWorkRows(false);
            initDegreeRows(false);
            initFragments(false);
            initFood(false);
            initStores(false);
            initFurnitures(false);
            initMedicine(false);
            myAppDataBase.myDao().initDBVersion(new VersionDB(getDatabaseVersion()));

            sharedPreferences.edit().putString("entry","available").apply();
        }
        else {

            String actualversion =getDatabaseVersion();
            VersionDB versionDB = myAppDataBase.myDao().getVersionDB();

            if(! versionDB.getVersion().equals(actualversion)){

                initWorkRows(true);
                initDegreeRows(true);
                initFragments(true);
                initFood(true);
                initStores(true);
                initFurnitures(true);
                initMedicine(true);

                versionDB.setVersion(actualversion);
                myAppDataBase.myDao().updateVerionDB(versionDB);
            }

        }




        newGame = findViewById(R.id.newgame);
        loadGame = findViewById(R.id.loadgame);
        settings = findViewById(R.id.settings);
        credits=findViewById(R.id.credits);


        imageView1=findViewById(R.id.imageView1);
        imageView2=findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);


        mainLayout=(ConstraintLayout)findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(mOnTouchListener);



        credits.setOnClickListener(view ->{


        });


        newGame.setOnClickListener(view ->{

                dialogueCreatorNewGame();
        });

        loadGame.setOnClickListener(view ->{

                dialogueCreatorLoadGame();

        });


        /*
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),SettingsActivity.class);

                startActivity(intent);

            }
        });
        */
        animateButton();


        firsttime  =4000;

        Thread thread = new Thread(() -> {

            while (!isDestroyed()){

                try {


                    Thread.sleep(firsttime);
                    firsttime=1500;
                   runOnUiThread(() -> animateInThread(imageView3));

                    Thread.sleep(1500);
                    runOnUiThread(() -> animateInThread(imageView4));

                    Thread.sleep(2000);
                    runOnUiThread(() -> animateInThread(imageView1));

                    Thread.sleep(1500);
                    runOnUiThread(() -> animateInThread(imageView2));

                }catch (InterruptedException e){
                    e.printStackTrace();
                }


            }
        });

        thread.start();


    }


    public void animateInThread(ImageView imageView){
        int randomNumber = (int) ((imagesResources().length-0)* Math.random());

        animateImagesAlphaToZero(imageView,4000);
        imageView.setImageResource(imagesResources()[randomNumber]);
        animateImageAlphaToOne(imageView,4000);

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


    //pour ecraser une partie lorsque le joueur utilise une slot deja prise
    public void overwriteSlotDilaog (final int slotNumber){

        final AlertDialog.Builder overwriteDialog = new AlertDialog.Builder(MainMenu.this);



        overwriteDialog.setMessage(getString(R.string.overwriteTitle))
                .setPositiveButton(getString(R.string.overwrite), (dialog,which) -> {
                    myAppDataBase.myDao().deletePlayer(myAppDataBase.myDao().getPlayer(slotNumber));
                        dialogInputCreate(slotNumber);

                })
                .setNegativeButton(getString(R.string.no), (dialog,which) -> {
                            dialog.cancel();
                            dialog.dismiss();
                });

        AlertDialog alertDialog= overwriteDialog.create();

        alertDialog.show();

    }


    //Affiche les slots pour charger une partie

    public void dialogueCreatorLoadGame() {

        final Dialog loadSlot = new Dialog(MainMenu.this);
        loadSlot.setTitle("Slot Load Game");
        loadSlot.setContentView(R.layout.choose_slot_newgame);

        TextView slotTitle1 = (TextView) loadSlot.findViewById(R.id.slotTitle1);
        TextView slotTitle2 = (TextView) loadSlot.findViewById(R.id.slotTitle2);
        TextView slotTitle3 = (TextView) loadSlot.findViewById(R.id.slotTitle3);

        TextView slotDate1 = (TextView) loadSlot.findViewById(R.id.slotDate1);
        TextView slotDate2 = (TextView) loadSlot.findViewById(R.id.slotDate2);
        TextView slotDate3 = (TextView) loadSlot.findViewById(R.id.slotDate3);



        ImageView slotImage1 = (ImageView) loadSlot.findViewById(R.id.slotImage1);
        ImageView slotImage2 = (ImageView) loadSlot.findViewById(R.id.slotImage2);
        ImageView slotImage3 = (ImageView) loadSlot.findViewById(R.id.slotImage3);

        RelativeLayout slot1 = loadSlot.findViewById(R.id.slotId1);
        RelativeLayout slot2 = loadSlot.findViewById(R.id.slotId2);
        RelativeLayout slot3 = loadSlot.findViewById(R.id.slotId3);


        //Player Slot 1
        Player player = myAppDataBase.myDao().getPlayer(1);
        if(player != null) {

            Uri imgUri1;
            if (!(player.getWork_image_path() == null))
                imgUri1 = Uri.parse(player.getWork_image_path());
            else
                imgUri1 = Uri.parse("android.resource://com.houbenz.android.lifesimulator/drawable/ic_empty");

            slotImage1.setImageURI(imgUri1);
            slotTitle1.setText(player.getName());
            slotDate1.setText(player.getCreation_date());

        }

        else

            slot1.setEnabled(false);

        //Player Slot 2

        player =myAppDataBase.myDao().getPlayer(2);
        if(player != null) {

        Uri imgUri2 ;
        if (!(player.getWork_image_path() == null ))
            imgUri2 = Uri.parse(player.getWork_image_path());
        else
            imgUri2 = Uri.parse("android.resource://com.houbenz.android.lifesimulator/drawable/ic_empty");

        slotImage2.setImageURI(imgUri2);
        slotTitle2.setText(player.getName());
        slotDate2.setText(player.getCreation_date());

        }

         else
            slot2.setEnabled(false);

        //Player Slot 3
        player=myAppDataBase.myDao().getPlayer(3);
        if(player != null) {

        Uri imgUri3;
        if (!(player.getWork_image_path() == null ))
            imgUri3 = Uri.parse(player.getWork_image_path());
        else
            imgUri3 = Uri.parse("android.resource://com.houbenz.android.lifesimulator/drawable/ic_empty");

        slotImage3.setImageURI(imgUri3);
        slotTitle3.setText(player.getName());
        slotDate3.setText(player.getCreation_date());

        }

         else
            slot3.setEnabled(false);


        //Events on click for slots 1 2 3

            slot1.setOnClickListener(view -> {

                Intent intent = new Intent(MainMenu.this, GameScene.class);

                intent.putExtra("slotNumber", 1);

                startActivity(intent);

                loadSlot.dismiss();
            });

            slot2.setOnClickListener(view -> {

                Intent intent = new Intent(MainMenu.this, GameScene.class);

                intent.putExtra("slotNumber", 2);

                startActivity(intent);

                loadSlot.dismiss();
            });

            slot3.setOnClickListener(view -> {

                Intent intent = new Intent(MainMenu.this, GameScene.class);

                intent.putExtra("slotNumber", 3);

                startActivity(intent);

                loadSlot.dismiss();
            });

            loadSlot.show();

        }



    //Affiche les slots pour un nouveau joueur
    public void dialogueCreatorNewGame(){
        final Dialog choose = new Dialog(MainMenu.this);
        choose.setTitle("Slot New Game");
        choose.setContentView(R.layout.choose_slot_newgame);

        ImageView slotImage1=(ImageView)choose.findViewById(R.id.slotImage1);
        ImageView slotImage2=(ImageView)choose.findViewById(R.id.slotImage2);
        ImageView slotImage3=(ImageView)choose.findViewById(R.id.slotImage3);

        TextView slotTitle1=(TextView)choose.findViewById(R.id.slotTitle1);
        TextView slotTitle2=(TextView)choose.findViewById(R.id.slotTitle2);
        TextView slotTitle3=(TextView)choose.findViewById(R.id.slotTitle3);

        TextView slotDate1=(TextView)choose.findViewById(R.id.slotDate1);
        TextView slotDate2=(TextView)choose.findViewById(R.id.slotDate2);
        TextView slotDate3=(TextView)choose.findViewById(R.id.slotDate3);

        RelativeLayout slot1 = choose.findViewById(R.id.slotId1);
        RelativeLayout slot2 = choose.findViewById(R.id.slotId2);
        RelativeLayout slot3 = choose.findViewById(R.id.slotId3);


        List<Player> playerList =myAppDataBase.myDao().getPlayers();

        for( Player player : playerList){


                //First Slot
                if (player.getId() == 1) {
                    Uri imgUri1;
                    if (!(player.getWork_image_path() == null || player.getWork_image_path().equals(getString(R.string.none))))
                        imgUri1 = Uri.parse(player.getWork_image_path());
                    else
                        imgUri1 = Uri.parse("android.resource://com.example.android." +
                                "testsharedpreferences/drawable/ic_empty");

                    slotImage1.setImageURI(imgUri1);
                    slotTitle1.setText(player.getName());
                    slotDate1.setText(player.getCreation_date());


                }


                //Second Slot
                if (player.getId() == 2) {
                    Uri imgUri2 = null;

                    if (!(player.getWork_image_path() == null || player.getWork_image_path().equals(getString(R.string.none))))
                        imgUri2 = Uri.parse(player.getWork_image_path());
                    else
                        imgUri2 = Uri.parse("android.resource://com.example.android." +
                                "testsharedpreferences/drawable/ic_empty");

                    slotImage2.setImageURI(imgUri2);
                    slotTitle2.setText(player.getName());
                    slotDate2.setText(player.getCreation_date());


                }

                //Third Slot
                if (player.getId() == 3) {
                    Uri imgUri3 = null;

                    if (!(player.getWork_image_path() == null || player.getWork_image_path().equals(getString(R.string.none))))
                        imgUri3 = Uri.parse(player.getWork_image_path());
                    else
                        imgUri3 = Uri.parse("android.resource://com.example.android." +
                                "testsharedpreferences/drawable/ic_empty");

                    slotImage3.setImageURI(imgUri3);
                    slotTitle3.setText(player.getName());
                    slotDate3.setText(player.getCreation_date());
                }
            }

        slot1.setOnClickListener(view ->{

            if(slotTitle1.getText().equals(getString(R.string.none))) {
                dialogInputCreate( 1);
                choose.dismiss();
            }
            else
            {
                overwriteSlotDilaog(1);
                choose.dismiss();
            }
        });
        slot2.setOnClickListener(view ->{

            if(slotTitle2.getText().equals(getString(R.string.none))) {
                dialogInputCreate( 2);
                choose.dismiss();
            }
            else
            {
                overwriteSlotDilaog(2);
                choose.dismiss();
            }
        });
        slot3.setOnClickListener(view ->{

            if(slotTitle3.getText().equals(getString(R.string.none))) {
                dialogInputCreate( 3);
                choose.dismiss();
            }
            else
            {
                overwriteSlotDilaog(3);
                choose.dismiss();
            }
        });


        choose.show();
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


    public void animateButton(){





        animateView(newGame,1500);
        animateView(loadGame,2000);
        animateView(settings,2500);
        animateView(credits,3000);




        animateTranslationImageX(imageView1,1500);
        animateTranslationImageX(imageView2,2000);
        animateTranslationImageX(imageView3,2500);
        animateTranslationImageX(imageView4,3000);



       // showRandomImage();

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

        int[] arrayImage =new int[10];
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
        return arrayImage;
    }


    public void initWorkRows(boolean isUpdate){
       ArrayList<beans.Work> worksBeans = beans.Work.workInit(getApplicationContext()) ;

       for(beans.Work bean : worksBeans){
           database.Work work = new database.Work();

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

            degree.setName(learn.getName());
            degree.setPrice(learn.getPrice());

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

                degree.setName(learns.get(i).getName());
                degree.setPrice(learns.get(i).getPrice());

                myAppDataBase.myDao().addDegree(degree);
            }
        }
    }


    public void initFood(boolean isUpdate){
        ArrayList<Food>  foods= Food.initFood(getApplicationContext());

        for(Food food : foods){
            database.Food foodDb = new database.Food();

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
            mainFragments.setName(buy.getName());
            mainFragments.setColor(buy.getColor());
            mainFragments.setImage_Uri(buy.getImagePath());

            myAppDataBase.myDao().addMainFragment(mainFragments);


        }
    }

    public void initStores(boolean isUpdate){
        ArrayList<Store> stores = Store.initStore(getApplicationContext());

        for (Store store : stores) {
            database.Store storeDb = new database.Store();
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

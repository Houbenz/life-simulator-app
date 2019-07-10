package com.houbenz.lifesimulator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import conf.Params;
import database.Acquired_Cars;
import database.Acquired_Furnitures;
import database.Acquired_Houses;
import database.Acquired_Stores;
import database.Acquired_degree;
import database.Gift;
import database.MyAppDataBase;
import database.Player;
import database.VersionDB;

import database.DatabaseInit;


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

    public static final int  MY_PERMISSIONS_REQUEST_EXTERNAL_STROGAE=11;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private com.google.android.gms.games.Player player;


    private class ObjectType{

        public static final int PLAYER=0;
        public static final int ACQ_HOUSES=1;
        public static final int ACQ_STORES=2;
        public static final int ACQ_CARS=3;
        public static final int ACQ_FURNITURES=4;
        public static final int ACQ_DEGRESS=5;

    }

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






        myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "life_simulatordb")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();


        sharedPreferences = getApplicationContext().getSharedPreferences("myShared", Context.MODE_PRIVATE);

        String entry = sharedPreferences.getString("entry", "none");

        String lang=sharedPreferences.getString("lang","none");

        if (entry.equals("none")) {
            initDatabase(false);
            myAppDataBase.myDao().initDBVersion(new VersionDB(DatabaseInit.getDatabaseVersion(getApplicationContext()), 1));

            sharedPreferences.edit().putString("entry", "available").apply();
            sharedPreferences.edit().putString("lang",Locale.getDefault().getLanguage()).apply();
        } else {

            String actualversion = DatabaseInit.getDatabaseVersion(getApplicationContext());
            VersionDB versionDB = myAppDataBase.myDao().getVersionDB();

            if (versionDB == null) {
                myAppDataBase.myDao().initDBVersion(new VersionDB(DatabaseInit.getDatabaseVersion(getApplicationContext()), 1));
                versionDB = myAppDataBase.myDao().getVersionDB();
            }


            if (!versionDB.getVersion().equals(actualversion)) {

                initDatabase(true);
                versionDB.setVersion(actualversion);
                myAppDataBase.myDao().updateVerionDB(versionDB);

            }

            if(!lang.equals(Locale.getDefault().getLanguage())){
                initDatabase(true);
                sharedPreferences.edit().putString("lang",Locale.getDefault().getLanguage()).apply();
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


        mainLayout = findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(mOnTouchListener);



        //get the leaderboard score
        credits.setOnClickListener(view -> {

            showLeaderBoard();
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


        //So we could sign in to google play games
        startSignInIntent();





        Button savegame =findViewById(R.id.savegame);


        savegame.setOnClickListener(view->{

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
               ObjectOutputStream oos = new ObjectOutputStream(bos);

                Player player=myAppDataBase.myDao().getPlayer(1);
                List<Acquired_Furnitures> acquired_furnitures = myAppDataBase.myDao().getAcquiredFurnitures(1);
                List<Acquired_degree> acquired_degrees =myAppDataBase.myDao().getAcquiredDegrees(1);
                List<Acquired_Cars> acquired_cars =myAppDataBase.myDao().getAcquiredCars(1);
                List<Acquired_Houses> acquired_houses =myAppDataBase.myDao().getAcquiredHouses(1);
                List<Acquired_Stores> acquired_stores =myAppDataBase.myDao().getAcquiredStores(1);

                oos.writeObject(player);
                oos.writeObject(acquired_furnitures);
                //oos.writeObject(acquired_degrees);
               // oos.writeObject(acquired_cars);
                //oos.writeObject(acquired_houses);
               // oos.writeObject(acquired_stores);

                bytearray =bos.toByteArray();
                oos.close();


                executeSaving();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //this is for unlocking achievments
            // Games.getAchievementsClient(this,GoogleSignIn.getLastSignedInAccount(this))
            //.unlock(getString(R.string.achievement_first_one));
            //.unlock(getString(R.string.achievement_second_one));
            // .unlock(getString(R.string.achievement_blazer_lazer));

        });

        //For reading bytes to objects
        Button showSave = findViewById(R.id.readfile);

        showSave.setOnClickListener(view ->{
            showSavedGamesGoogleUi();
        });

        Button achievement = findViewById(R.id.achievement);

        achievement.setOnClickListener(view ->{

            getAchievements();
        });


        Button loadData=findViewById(R.id.loaddata);
        loadData.setOnClickListener(view ->{

           loadSnapshotData().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(getApplicationContext(),"succeful loading",Toast.LENGTH_SHORT).show();

                   //show the loaded data
                   Log.i("YUUP","length : "+task.getResult().length);

                   extractData(task.getResult());

               }else{

                   Toast.makeText(getApplicationContext(),"loading failed",Toast.LENGTH_SHORT).show();
               }
           });
        });
    }



    private Task<byte[]> loadSnapshotData(){
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(this,account);

        int conflictResolutionPolicy=SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

        return snapshotsClient.open(currentSave,false,conflictResolutionPolicy)
                .addOnFailureListener(e -> {

                    Log.e("ERR","snapshot"+e);

                }).continueWith(task -> {

                    Snapshot snapshot = task.getResult().getData();

                    try {
                        return snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        Log.e("ERRO", "snapshot" + e);
                    }

                    return null;
                });
    }

    private  void extractData(byte[] rawData) {
        ByteArrayInputStream byteArrayInputStream =new ByteArrayInputStream(rawData);
        try {
            ObjectInputStream objectInputStream =new ObjectInputStream(byteArrayInputStream);

            Player player =(Player)objectInputStream.readObject();
            List<Acquired_Furnitures> acquired_furnitures=(List<Acquired_Furnitures>) objectInputStream.readObject();

            Log.i("YUUP","its loaded : "+ player.getName());

            Log.i("YUUP","here to is loaded "+acquired_furnitures.get(0).getImgurl());


            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    byte[] bytearray;

    private String currentSave="snapshot"+1235;

    private void executeSaving(){


        SnapshotsClient snapshotsClient= Games.getSnapshotsClient(this,account);

        // to save a game
        snapshotsClient.open(currentSave,true).addOnCompleteListener(task -> {

            Snapshot snapshot =task.getResult().getData();

            if (snapshot != null){

                writeSnapshot(snapshot,bytearray,"first description").addOnCompleteListener(task1 -> {

                    if(task1.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Succesful",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.e("ERR",""+task1.getException());
                    }
                });
            }
        });
    }
    private Task<SnapshotMetadata> writeSnapshot(Snapshot snapshot ,byte[] bytearray,String desc){

        snapshot.getSnapshotContents().writeBytes(bytearray);

        SnapshotMetadataChange snapshotMetadata = new SnapshotMetadataChange.Builder().setDescription(desc).build();

        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(this,account);


        return snapshotsClient.commitAndClose(snapshot,snapshotMetadata);
    }


    private void startSignInIntent(){

        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
                if(GoogleSignIn.hasPermissions(account)){

                    this.account=account;
                }
        }else{

            mGoogleSignInClient = GoogleSignIn.getClient(this,
                    signInOptions);

            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }
    }



    public void getPlayerInformation(){

        ProgressBar progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.VISIBLE);
        //Player clients is used to get all available playes
        PlayersClient playersClient = Games.getPlayersClient(getApplicationContext(),account);

        //get the current player
        playersClient.getCurrentPlayer().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                player = task.getResult();

                progressBar.setVisibility(View.GONE);
            }
        });
    }



    //for loading all the achievements !
    public static final int RC_ACHEIVEMENTS=1110;
    public void getAchievements(){

    Games.getAchievementsClient(this,account)
            .getAchievementsIntent()
            .addOnSuccessListener(intent ->{

                startActivityForResult(intent,RC_ACHEIVEMENTS);
            });
    }

    private void settingUpPopUpforAchievements(){
        GamesClient gamesClient = Games.getGamesClient(this,account);
        gamesClient.setViewForPopups(findViewById(android.R.id.content));
        gamesClient.setGravityForPopups(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    }


    private void showLeaderBoard(){

        Games.getLeaderboardsClient(this,account)
                .getLeaderboardIntent(getString(R.string.leaderboard_score))
                .addOnSuccessListener(intent -> {
                    startActivityForResult(intent,RC_LEADERBOARD_SCORE);
                });
    }

    public static final int RC_SAVE_GAME=2000;

    private void showSavedGamesGoogleUi(){
        SnapshotsClient snapshotsClient= Games.getSnapshotsClient(this,account);

        int numSaves = 1;

        Task<Intent> intentTask=snapshotsClient.getSelectSnapshotIntent("See my saves",true,true,numSaves);

        intentTask.addOnSuccessListener(intent ->{

            startActivityForResult(intent,RC_SAVE_GAME);
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result != null) {
                if (result.isSuccess()) {

                    account = result.getSignInAccount();
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                    //so we can get the players info
                    getPlayerInformation();

                    //setting pop up for achievements !
                    settingUpPopUpforAchievements();

                    Log.i("UUU", "Sol7ot");
                } else {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    public void initDatabase(Boolean update){


        List<database.Work> works=myAppDataBase.myDao().getWorks();

        if(!works.isEmpty()) {
            for (database.Work work : works) {
                Log.i("YUUP", work.getName() + "");
            }
        }else

            Log.i("YUUP", "no work is in there aw ga3ed ya3ti wahdo");

        DatabaseInit.initWorkRows(update,getApplicationContext());
        DatabaseInit.initDegreeRows(update,getApplicationContext());
        DatabaseInit.initFragments(update,getApplicationContext());
        DatabaseInit.initFood(update,getApplicationContext());
        DatabaseInit.initStores(update,getApplicationContext());
        DatabaseInit.initFurnitures(update,getApplicationContext());
        DatabaseInit.initMedicine(update,getApplicationContext());
        DatabaseInit.initCars(update,getApplicationContext());
        DatabaseInit.initPartners(update,getApplicationContext());
        DatabaseInit.initHouses(update,getApplicationContext());
        DatabaseInit.initGifts(update,getApplicationContext());
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
    public void dialogInputCreate(int slotNumber){



        final Dialog putText = new Dialog(MainMenu.this);

        putText.setTitle("put your name");

        putText.setContentView(R.layout.caracter_name);

        final TextInputLayout caracterName = putText.findViewById(R.id.nameInput);

        Button closeDialog =putText.findViewById(R.id.closeDialog);

        closeDialog.setOnClickListener(view -> {

                putText.dismiss();
        });

        if(player != null)
        caracterName.getEditText().setText(player.getDisplayName());

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
                    player.setMarried("false");
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
    public void overwriteSlotDilaog(int slotNumber) {

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

        int[] arrayImage =new int[18];
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
        arrayImage[13]=R.drawable.glasses_girl;
        arrayImage[14]=R.drawable.gift;
        arrayImage[15]=R.drawable.chocolate;
        arrayImage[16]=R.drawable.crown_hair;
        arrayImage[17]=R.drawable.roses;
        arrayImage[17]=R.drawable.jewelry;
        return arrayImage;
    }
}

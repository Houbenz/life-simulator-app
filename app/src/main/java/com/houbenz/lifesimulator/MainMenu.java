package com.houbenz.lifesimulator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import conf.Params;
import database.Acquired_Cars;
import database.Acquired_Furnitures;
import database.Acquired_Houses;
import database.Acquired_Stores;
import database.Acquired_degree;
import database.DatabaseInit;
import database.Gift;
import database.MyAppDataBase;
import database.Partner;
import database.Player;
import database.VersionDB;


public class MainMenu extends AppCompatActivity {


    private Button newGame;
    private Button loadGame;
    private Button achievements;
    private Button credits;

    public static MyAppDataBase myAppDataBase;


    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private int firstTime;

    private ConstraintLayout mainLayout;

    private SharedPreferences sharedPreferences;


    private static final int RC_SIGN_IN = 106 ;
    private static final int RC_LEADERBOARD_SCORE =1005;

    public static final int  MY_PERMISSIONS_REQUEST_EXTERNAL_STROGAE=11;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private com.google.android.gms.games.Player player;

    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnClickListener mOnTouchListener = (v) -> {

        //delayedHide();
        hideSystemUI();
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
        achievements = findViewById(R.id.achievements);
        credits = findViewById(R.id.credits);


        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        progressBar=findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);

        mainLayout.setOnClickListener(mOnTouchListener);



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

        achievements.setOnClickListener(view->{

            getAchievements();
        });



        animateButton();


        firstTime = 4000;

        Thread thread = new Thread(() -> {

            while (!isDestroyed()) {

                try {


                    Thread.sleep(firstTime);
                    firstTime = 1500;
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

    }


    private Task<byte[]> loadSnapshotData(){
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(this,account);

        int conflictResolutionPolicy=SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

        return snapshotsClient.open(currentSave,false,conflictResolutionPolicy)
                .addOnFailureListener(Throwable::printStackTrace).continueWith(task -> {

                    Snapshot snapshot = task.getResult().getData();

                    try {
                        return snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                });
    }

    private  void extractData(byte[] rawData) {
        ByteArrayInputStream byteArrayInputStream =new ByteArrayInputStream(rawData);
        try {
            ObjectInputStream ois =new ObjectInputStream(byteArrayInputStream);

            //fetch the date from rawData
            Player player =(Player)ois.readObject();
            List<Acquired_Furnitures> acquired_furnitures=(List<Acquired_Furnitures>) ois.readObject();
            List<Acquired_degree> acquired_degrees=(List<Acquired_degree>) ois.readObject();
            List<Acquired_Cars> acquired_cars=(List<Acquired_Cars>) ois.readObject();
            List<Acquired_Houses> acquired_houses=(List<Acquired_Houses>) ois.readObject();
            List<Acquired_Stores> acquired_stores=(List<Acquired_Stores>) ois.readObject();
            List<Partner> partners = (List<Partner>)ois.readObject();
            List<Gift> gifts = (List<Gift>)ois.readObject();

            /*
             * insert the objects into the database
             */

            if(player !=  null)
            myAppDataBase.myDao().addPlayer(player);

            if(acquired_furnitures!= null)
            for(Acquired_Furnitures acq : acquired_furnitures){
                myAppDataBase.myDao().addAcquired_Furniture(acq);
            }

            if(acquired_degrees != null)
            for(Acquired_degree acq :acquired_degrees){
                myAppDataBase.myDao().addAcquired_degree(acq);
            }

            if(acquired_cars != null)
            for(Acquired_Cars acq:acquired_cars){
                myAppDataBase.myDao().addAcquired_Car(acq);
            }

            if(acquired_houses != null)
            for(Acquired_Houses acq :acquired_houses){
                myAppDataBase.myDao().addAcquired_House(acq);
            }

            if(acquired_stores != null)
            for(Acquired_Stores acq : acquired_stores){
                myAppDataBase.myDao().addAcquired_Store(acq);
            }

            if(partners != null)
                for (Partner partner : partners){
                    myAppDataBase.myDao().updatePartner(partner);
            }

            if(gifts != null)
                for(Gift gift: gifts){
                    myAppDataBase.myDao().updateGift(gift);
            }
            ois.close();

            progressBar.setVisibility(View.GONE);

            sharedPreferences.edit().putString("firstTime", "finished").apply();
            Log.i("okkk","firstTime : "+sharedPreferences.getString("firstTime","none"));
            Intent intent = new Intent(MainMenu.this, GameScene.class);
            intent.putExtra("slotNumber", 1);
            startActivity(intent);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final String  currentSave="snapshot"+1235;

    private void startSignInIntent(){

        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
                if(GoogleSignIn.hasPermissions(account)){

                    this.account=account;
                }
                Log.e("ERRA","account "+ this.account.toString());
        }else{

            mGoogleSignInClient = GoogleSignIn.getClient(this,
                    signInOptions);

            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }
    }



    public void getPlayerInformation(){

        //Player clients is used to get all available playes
        PlayersClient playersClient = Games.getPlayersClient(getApplicationContext(),account);

        //get the current player
        playersClient.getCurrentPlayer().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                player = task.getResult();

            }
        });
    }



    //for loading all the achievements !
    public static final int RC_ACHEIVEMENTS=1110;
    public void getAchievements(){

        if(account != null && GoogleSignIn.hasPermissions(account)) {
            Games.getAchievementsClient(this, account)
                    .getAchievementsIntent()
                    .addOnSuccessListener(intent -> {

                        startActivityForResult(intent, RC_ACHEIVEMENTS);
                    });
        }
    }

    private void settingUpPopUpforAchievements(){
        GamesClient gamesClient = Games.getGamesClient(this,account);
        gamesClient.setViewForPopups(findViewById(android.R.id.content));
        gamesClient.setGravityForPopups(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    }


    private void showLeaderBoard(){


        if(account != null && GoogleSignIn.hasPermissions(account)) {
            Games.getLeaderboardsClient(this, account)
                    .getLeaderboardIntent(getString(R.string.leaderboard_score))
                    .addOnSuccessListener(intent -> {
                        startActivityForResult(intent, RC_LEADERBOARD_SCORE);
                    });
        }
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
                    //so we can get the players info
                    getPlayerInformation();

                    //setting pop up for achievements !
                    settingUpPopUpforAchievements();

                } else {
                    Log.e("ERRA","Status :  "+result.getStatus()+" result "+result.getSignInAccount());
                    Toast.makeText(getApplicationContext(), "failed to login to google play games", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    public void initDatabase(Boolean update){
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


        progressBar.setIndeterminate(true);

        progressBar.setVisibility(View.VISIBLE);
        Player player = myAppDataBase.myDao().getPlayer(1);

        if(player != null) {

            sharedPreferences.edit().putString("firstTime", "finished").apply();
            Log.i("okkk","firstTime : "+sharedPreferences.getString("firstTime","none"));
            Intent intent = new Intent(MainMenu.this, GameScene.class);
            intent.putExtra("slotNumber", 1);
            progressBar.setVisibility(View.GONE);
            startActivity(intent);
        }else {
            if (this.account != null && GoogleSignIn.hasPermissions(account)) {

                loadSnapshotData().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        extractData(task.getResult());

                        Toast.makeText(getApplicationContext(), "Loading succesful", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Loading failed !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            progressBar.setVisibility(View.GONE);
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
        animateView(achievements,2500);
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

package com.example.android.testsharedpreferences;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import beans.Work;

public class MainMenu extends AppCompatActivity {


    private Button newGame;
    private Button loadGame;
    private Button settings;
    private Button credits;
    private SharedPreferences sharedPreferences1 ;
    private SharedPreferences sharedPreferences2 ;
    private SharedPreferences sharedPreferences3 ;

    private LinearLayout linearLayout;


    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);






        newGame = findViewById(R.id.newgame);
        loadGame = findViewById(R.id.loadgame);
        settings = findViewById(R.id.settings);
        credits=findViewById(R.id.credits);


        imageView1=findViewById(R.id.imageView1);
        imageView2=findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);



        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);


        sharedPreferences1 = getApplicationContext().getSharedPreferences(getString(R.string.prefSlot1), Context.MODE_PRIVATE);
        sharedPreferences2 = getApplicationContext().getSharedPreferences(getString(R.string.prefSlot2), Context.MODE_PRIVATE);
        sharedPreferences3 = getApplicationContext().getSharedPreferences(getString(R.string.prefSlot3), Context.MODE_PRIVATE);

        //resetAll();

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogueCreatorNewGame();
            }
        });

        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogueCreatorLoadGame();
            }
        });




        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        animateButton();
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
    public void overwriteSlotDilaog (final SharedPreferences.Editor editor , final int slotNumber){

        final AlertDialog.Builder overwriteDialog = new AlertDialog.Builder(MainMenu.this);



        overwriteDialog.setMessage("would you like to overwrite this slot ?")
                .setPositiveButton("overwrite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialogInputCreate(editor , slotNumber);


                    }
                })
                .setNegativeButton("nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                    }
                });

        AlertDialog alertDialog= overwriteDialog.create();

        alertDialog.show();

    }




    //Affiche les slots pour charger une partie

    public void dialogueCreatorLoadGame(){

        final Dialog loadSlot = new Dialog(MainMenu.this);
        loadSlot.setTitle("Slot Load Game");
        loadSlot.setContentView(R.layout.choose_slot_newgame);


        String title1=sharedPreferences1.getString("PlayerName","none");
        String title2=sharedPreferences2.getString("PlayerName","none");
        String title3=sharedPreferences3.getString("PlayerName","none");

        String date1=sharedPreferences1.getString("PlayerTime","none");
        String date2=sharedPreferences2.getString("PlayerTime","none");
        String date3=sharedPreferences3.getString("PlayerTime","none");



        TextView slotTitle1=(TextView)loadSlot.findViewById(R.id.slotTitle1);
        TextView slotTitle2=(TextView)loadSlot.findViewById(R.id.slotTitle2);
        TextView slotTitle3=(TextView)loadSlot.findViewById(R.id.slotTitle3);

        TextView slotDate1=(TextView)loadSlot.findViewById(R.id.slotDate1);
        TextView slotDate2=(TextView)loadSlot.findViewById(R.id.slotDate2);
        TextView slotDate3=(TextView)loadSlot.findViewById(R.id.slotDate3);


        slotTitle1.setText(title1);
        slotTitle2.setText(title2);
        slotTitle3.setText(title3);

        slotDate1.setText(date1);
        slotDate2.setText(date2);
        slotDate3.setText(date3);

        //
        String imagePath1=sharedPreferences1.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty");
        String imagePath2=sharedPreferences2.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty");
        String imagePath3=sharedPreferences3.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty");

        Uri imgUri1=Uri.parse(imagePath1);
        Uri imgUri2=Uri.parse(imagePath2);
        Uri imgUri3=Uri.parse(imagePath3);


        ImageView slotImage1=(ImageView)loadSlot.findViewById(R.id.slotImage1);
        ImageView slotImage2=(ImageView)loadSlot.findViewById(R.id.slotImage2);
        ImageView slotImage3=(ImageView)loadSlot.findViewById(R.id.slotImage3);

        slotImage1.setImageURI(imgUri1);
        slotImage2.setImageURI(imgUri2);
        slotImage3.setImageURI(imgUri3);

        RelativeLayout slot1 = loadSlot.findViewById(R.id.slotId1);
        RelativeLayout slot2 = loadSlot.findViewById(R.id.slotId2);
        RelativeLayout slot3 = loadSlot.findViewById(R.id.slotId3);


        if(title1.equals("none"))
            slot1.setEnabled(false);

        if(title2.equals("none"))
            slot2.setEnabled(false );


        if(title3.equals("none"))
            slot3.setEnabled(false);

            slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this,GameScene.class);

                intent.putExtra("slotNumber",1);

                startActivity(intent);

                loadSlot.dismiss();
            }
        });

        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this,GameScene.class);

                intent.putExtra("slotNumber",2);

                startActivity(intent);

                loadSlot.dismiss();


            }
        });

        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this,GameScene.class);

                intent.putExtra("slotNumber",3);

                startActivity(intent);
                loadSlot.dismiss();


            }
        });

        loadSlot.show();

    }


    //Affiche les slots pour un nouveau joueur
    public void dialogueCreatorNewGame(){
        final Dialog choose = new Dialog(MainMenu.this);
        choose.setTitle("Slot New Game");
        choose.setContentView(R.layout.choose_slot_newgame);

        final String title1=sharedPreferences1.getString("PlayerName","none");
        final String title2=sharedPreferences2.getString("PlayerName","none");
        final String title3=sharedPreferences3.getString("PlayerName","none");

        String date1=sharedPreferences1.getString("PlayerTime","none");
        String date2=sharedPreferences2.getString("PlayerTime","none");
        String date3=sharedPreferences3.getString("PlayerTime","none");

        String imagePath1=sharedPreferences1.getString("imagePath","android.resource://com.example.android." +
        "testsharedpreferences/drawable/ic_empty");
        String imagePath2=sharedPreferences2.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty");
        String imagePath3=sharedPreferences3.getString("imagePath","android.resource://com.example.android." +
                "testsharedpreferences/drawable/ic_empty");

        Uri imgUri1=Uri.parse(imagePath1);
        Uri imgUri2=Uri.parse(imagePath2);
        Uri imgUri3=Uri.parse(imagePath3);


        ImageView slotImage1=(ImageView)choose.findViewById(R.id.slotImage1);
        ImageView slotImage2=(ImageView)choose.findViewById(R.id.slotImage2);
        ImageView slotImage3=(ImageView)choose.findViewById(R.id.slotImage3);

        slotImage1.setImageURI(imgUri1);
        slotImage2.setImageURI(imgUri2);
        slotImage3.setImageURI(imgUri3);

        TextView slotTitle1=(TextView)choose.findViewById(R.id.slotTitle1);
        TextView slotTitle2=(TextView)choose.findViewById(R.id.slotTitle2);
        TextView slotTitle3=(TextView)choose.findViewById(R.id.slotTitle3);

        TextView slotDate1=(TextView)choose.findViewById(R.id.slotDate1);
        TextView slotDate2=(TextView)choose.findViewById(R.id.slotDate2);
        TextView slotDate3=(TextView)choose.findViewById(R.id.slotDate3);



        slotTitle1.setText(title1);
        slotTitle2.setText(title2);
        slotTitle3.setText(title3);

        slotDate1.setText(date1);
        slotDate2.setText(date2);
        slotDate3.setText(date3);

        RelativeLayout slot1 = choose.findViewById(R.id.slotId1);
        RelativeLayout slot2 = choose.findViewById(R.id.slotId2);
        RelativeLayout slot3 = choose.findViewById(R.id.slotId3);


       final SharedPreferences.Editor shaprefEditor1 = sharedPreferences1.edit();
       final SharedPreferences.Editor shaprefEditor2 = sharedPreferences2.edit();
       final SharedPreferences.Editor shaprefEditor3 = sharedPreferences3.edit();



        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(title1.equals("none")) {
                    dialogInputCreate(shaprefEditor1, 1);
                    choose.dismiss();
                }
                else
                {
                    overwriteSlotDilaog(shaprefEditor1,1);
                    choose.dismiss();
                }
            }
        });

        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(title2.equals("none")) {
                    dialogInputCreate(shaprefEditor2, 2);
                    choose.dismiss();
                }else {
                    overwriteSlotDilaog(shaprefEditor2,2);
                    choose.dismiss();
                }
            }

        });

        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(title3.equals("none")){
                dialogInputCreate(shaprefEditor3, 3);
                choose.dismiss();
                 }
                 else
                {
                    overwriteSlotDilaog(shaprefEditor3,3);
                    choose.dismiss();
                }
            }
        });
        choose.show();
    }



    //Pour entrer le nom du joueur puis sauvgarder la premiere session

    public void dialogInputCreate(final SharedPreferences.Editor editor , final int slotNumber){

        final Dialog putText = new Dialog(MainMenu.this);

        putText.setTitle("put your name");
        //TODO
        putText.setContentView(R.layout.caracter_name);

        final TextInputLayout caracterName = putText.findViewById(R.id.nameInput);



        Button closeDialog =putText.findViewById(R.id.closeDialog);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putText.dismiss();
            }
        });

        Button confirmDialog =putText.findViewById(R.id.confirm);

        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(caracterName.getEditText().getText().toString().equals("")) {
                    caracterName.setError("you must write at least 1 caracter");
                }
                else
                {
                    editor.clear().apply();

                    editor.putString("PlayerName", caracterName.getEditText().getText().toString());

                    Calendar cal = Calendar.getInstance();
                    editor.putString("PlayerTime", cal.get(Calendar.DATE) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR)
                            + " at " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));

                    editor.commit();

                    putText.dismiss();

                    Intent intent = new Intent(MainMenu.this, GameScene.class);

                    intent.putExtra("slotNumber", slotNumber);

                    startActivity(intent);

                }

            }
        });

        putText.show();
    }



    public void resetAll(){
        sharedPreferences1.edit().clear().apply();
        sharedPreferences2.edit().clear().apply();
        sharedPreferences3.edit().clear().apply();
    }

    public void animateButton(){




        newGame.setY(-1000);
        loadGame.setY(-1200);
        settings.setY(-1400);
        credits.setY(-1600);
        animateView(newGame);
        animateView(loadGame);
        animateView(settings);


        credits.animate().setDuration(4500).translationY(0f).withEndAction(new Runnable() {
            @Override
            public void run() {

                linearLayout.getLayoutParams().width= LinearLayout.LayoutParams.WRAP_CONTENT;

            }
        });


        animateImages(imageView1,5000);
        animateImages(imageView2,8000);
        animateImages(imageView3,3000);
        animateImages(imageView4,4000);
    }


    public void animateView(View view){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,view.TRANSLATION_Y,0f);
        objectAnimator.setDuration(4500);
        objectAnimator.start();




    }


    public void animateImages(ImageView imageView,int duration){
        ObjectAnimator objectAnimator =ObjectAnimator.ofFloat(imageView, imageView.ALPHA,1f,0f);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();

    }


}

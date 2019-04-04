package com.houbenz.lifesimulator;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.android.houbenz.lifesimulator.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {



    ConstraintLayout mainLayout;


    MediaPlayer mp;

    SeekBar seekBarSound ;

    private View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideSystemUi();

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        hideSystemUi();

        mainLayout=findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(onTouchListener);

        ImageView frenchFlag =findViewById(R.id.frenchImg);

        frenchFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
            }
        });

        ImageView usaFlag =findViewById(R.id.usaImg);

        usaFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });



        seekBarSound=findViewById(R.id.seekBarSound);

        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });



    }



    public void hideSystemUi(){
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }



    public void setLocale(String loc){
        Locale choosenLocal =new Locale(loc);

        Locale.setDefault(choosenLocal);

        Resources res =getResources();


        Configuration configuration =new Configuration(res.getConfiguration());
        configuration.setLocale(choosenLocal);

        createConfigurationContext(configuration);

        Intent refresh =new Intent(getApplicationContext(),SettingsActivity.class);

      //  startActivity(refresh);
        //finish();

    }
}

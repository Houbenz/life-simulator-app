package com.houbenz.lifesimulator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.houbenz.lifesimulator.R;

import fragments.HomeFragment;


public class HomeActivity extends AppCompatActivity implements HomeFragment.homeShow {

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Button showHomeButton;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showHomeButton=findViewById(R.id.showHomeButton);

        showHomeButton.setOnClickListener(view ->{

            HomeFragment homeFragment = new HomeFragment();
            insertFragment(homeFragment);

        });

    }


    public void insertFragment(Fragment fragment){

        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        transaction =fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);

        transaction.replace(R.id.placeFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onHomeShow(String url) {

    }
}

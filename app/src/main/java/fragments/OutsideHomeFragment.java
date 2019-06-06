package fragments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import database.Acquired_Cars;
import database.Acquired_Houses;
import database.House;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutsideHomeFragment extends Fragment {


    ImageView outsideView;
    ImageView carPlace;
    ImageView garage;

    private final String HOUSE_GARAGE="android.resource://com.houbenz.android.lifesimulator/drawable/outside_house_with_garage";
    private final String HOUSE_NO_GARAGE="android.resource://com.houbenz.android.lifesimulator/drawable/outside_house_no_garage";

    public OutsideHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment;





        int slot = getArguments().getInt("slot");
        Acquired_Houses acquired_houses = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,1);

        if(acquired_houses == null) {

            fragment = inflater.inflate(R.layout.fragment_home_not_owned, container, false);
        }else
        {
            fragment = inflater.inflate(R.layout.fragment_outside_home, container, false);


            outsideView=fragment.findViewById(R.id.outsideView);
            garage=fragment.findViewById(R.id.garage);
            carPlace=fragment.findViewById(R.id.carPlace);

            Acquired_Houses acquired_garage = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,2);

            if(acquired_garage!=null){
                garage.setImageURI(Uri.parse(acquired_garage.getImgUrl()+"x"));
            }

           List<Acquired_Cars> acquired_cars=MainMenu.myAppDataBase.myDao().getAcquiredCars(slot);
            for(Acquired_Cars acqCar : acquired_cars){
                if(acqCar != null){
                    if(acqCar.getAvailable().equals("true")){
                        carPlace.setImageURI(Uri.parse(acqCar.getImgUrl()+"x"));
                    }
                }
            }
        }

        View mainConsLayout = fragment.findViewById(R.id.mainConsLayout);

        CountDownTimer count = new CountDownTimer(500,250) {
            @Override
            public void onTick(long millisUntilFinished) {
                outsideView.setImageBitmap(getBitmap(mainConsLayout));
            }

            @Override
            public void onFinish() {

            removeAllImages();

            }
        };
        count.start();


        return fragment;
    }


    private void removeAllImages(){
        carPlace.setVisibility(View.GONE);
        garage.setVisibility(View.GONE);
    }

    private Bitmap getBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}

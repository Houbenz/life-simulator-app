package fragments;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import database.Acquired_Furnitures;
import database.Acquired_Houses;
import viewmodels.ViewModelFourHome;

import static com.houbenz.lifesimulator.MainMenu.myAppDataBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {



   // private ImageView room;
    private ImageView salonTable;
    private ImageView tablePlace;
    private ImageView tvPlace;
    private ImageView couch;
    private ImageView chair;
    private ImageView room;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragment;




        int slot = getArguments().getInt("slot");


       Acquired_Houses acquired_houses = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,1);

       if(acquired_houses == null){


           fragment =inflater.inflate(R.layout.fragment_home_not_owned, container, false);

       }else {

           fragment =inflater.inflate(R.layout.fragment_home, container, false);

           salonTable=fragment.findViewById(R.id.salontable);
           tablePlace=fragment.findViewById(R.id.tablePlace);
           tvPlace=fragment.findViewById(R.id.tvPlace);
           couch=fragment.findViewById(R.id.couch);
           chair=fragment.findViewById(R.id.chair);
           room=fragment.findViewById(R.id.room);

           //get all acquired furnitures
           List<Acquired_Furnitures> acquired_furnituresList = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(slot);

           for (Acquired_Furnitures furniture : acquired_furnituresList) {
               Uri uri;
               switch (furniture.getFurnitureType()) {

                   case "table":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           tablePlace.setImageURI(uri);
                       }
                       break;

                   case "tv":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           tvPlace.setImageURI(uri);
                       }
                       break;

                   case "chair":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           chair.setImageURI(uri);
                       }
                       break;

                   case "computer":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           tablePlace.setImageURI(uri);
                       }
                       break;


                   case "couch":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           couch.setImageURI(uri);
                       }
                       break;

                   case "salonTable":
                       if (furniture.getAvailable().equals("true")) {
                           uri = Uri.parse(furniture.getImgurl()+"x");
                           salonTable.setImageURI(uri);
                       }
                       break;
                   default:
                       ;


               }

           }


       View introLayout = fragment.findViewById(R.id.introLayout);


        CountDownTimer count = new CountDownTimer(500,250) {
            @Override
            public void onTick(long millisUntilFinished) {
                room.setImageBitmap(getBitmap(introLayout));
            }

            @Override
            public void onFinish() {

                removeAllImages();
            }
        };
        count.start();


       }
        return fragment;
    }


    private Bitmap getBitmap(@NonNull View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void removeAllImages(){
        chair.setVisibility(View.GONE);
        tvPlace.setVisibility(View.GONE);
        tablePlace.setVisibility(View.GONE);
        couch.setVisibility(View.GONE);
        salonTable.setVisibility(View.GONE);
    }
}

package fragments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import database.Acquired_Furnitures;
import database.Acquired_Houses;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.List;


public class Home2Fragment extends Fragment {


    private ImageView room2;
    private ImageView wardrobe;
    private ImageView computerTable;
    private ImageView bed;

    public Home2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        int slot = getArguments().getInt("slot");
        View fragment;


        Acquired_Houses acquired_houses = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,3);

        if(acquired_houses == null){


            fragment =inflater.inflate(R.layout.fragment_home_not_owned, container, false);

        }else {

            fragment = inflater.inflate(R.layout.fragment_home2, container, false);

            room2 = fragment.findViewById(R.id.room2);
            wardrobe = fragment.findViewById(R.id.wardrobe);
            computerTable = fragment.findViewById(R.id.computer_table);
            bed = fragment.findViewById(R.id.bed);


            List<Acquired_Furnitures> acquired_furnituresList = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(slot);


            for(Acquired_Furnitures furn : acquired_furnituresList){
                Uri uri=null;
                switch (furn.getFurnitureType()){

                    case "wardrobe" :
                        if(furn.getAvailable().equals("true"))
                        uri = Uri.parse(furn.getImgurl()+"x");
                        wardrobe.setImageURI(uri);
                        break;


                    case "computer table" :

                        if(furn.getAvailable().equals("true"))
                            uri = Uri.parse(furn.getImgurl()+"x");
                        computerTable.setImageURI(uri);

                        break;
                    case "bed" :

                        if(furn.getAvailable().equals("true"))
                            uri = Uri.parse(furn.getImgurl()+"x");
                        bed.setImageURI(uri);
                        break;

                        default: ;
                }
            }



        View introLayout = fragment.findViewById(R.id.introlayout);


        CountDownTimer count = new CountDownTimer(500,250) {
            @Override
            public void onTick(long millisUntilFinished) {
                room2.setImageBitmap(getBitmap(introLayout));
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

    private  void removeAllImages(){
        bed.setVisibility(View.GONE);
        wardrobe.setVisibility(View.GONE);
        computerTable.setVisibility(View.GONE);
    }

    private Bitmap getBitmap(@NonNull View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}

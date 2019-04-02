package fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import database.Acquired_Furnitures;
import viewmodels.ViewModelFourHome;

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
    private ViewModelFourHome viewModel;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewModel= ViewModelProviders.of(getActivity()).get(ViewModelFourHome.class);

        View fragment =inflater.inflate(R.layout.fragment_home, container, false);

        salonTable=fragment.findViewById(R.id.salontable);
        tablePlace=fragment.findViewById(R.id.tablePlace);
        tvPlace=fragment.findViewById(R.id.tvPlace);
        couch=fragment.findViewById(R.id.couch);
        chair=fragment.findViewById(R.id.chair);


        int slot = getArguments().getInt("slot");

        //get all acquired furnitures
        List<Acquired_Furnitures> acquired_furnituresList = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(slot);

        for (Acquired_Furnitures furniture : acquired_furnituresList ){
            Uri uri;
            switch (furniture.getFurnitureType()){

                case "table" :
                    if(furniture.getAvailable().equals("true")) {
                        uri = Uri.parse(furniture.getImgurl());
                        tablePlace.setImageURI(uri);
                    }
                    break;

                case "tv"    :
                    if(furniture.getAvailable().equals("true")) {
                    uri = Uri.parse(furniture.getImgurl());
                    tvPlace.setImageURI(uri);
                    }
                    break;

                case "chair" :
                    if(furniture.getAvailable().equals("true")) {
                    uri = Uri.parse(furniture.getImgurl());
                    chair.setImageURI(uri);
                    }
                    break;

                case "computer" :
                    if(furniture.getAvailable().equals("true")) {
                    uri = Uri.parse(furniture.getImgurl());
                    tablePlace.setImageURI(uri);
                    }
                    break;


                case "couch" :
                    if(furniture.getAvailable().equals("true")) {
                    uri = Uri.parse(furniture.getImgurl());
                    couch.setImageURI(uri);
                    }
                    break;

                case "salonTable" :
                    if(furniture.getAvailable().equals("true")) {
                    uri = Uri.parse(furniture.getImgurl());
                    salonTable.setImageURI(uri);
                    }
                    break;
                default: ;


            }

        }

        return fragment;
    }

}

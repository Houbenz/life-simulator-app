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

import com.example.android.testsharedpreferences.R;

import beans.Furniture;
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
    private ViewModelFourHome viewModel;
    private homeShow monStartWork;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        String url ="" ;
        monStartWork.onHomeShow(url);

        viewModel= ViewModelProviders.of(getActivity()).get(ViewModelFourHome.class);

        View fragment =inflater.inflate(R.layout.fragment_home, container, false);

       // room=fragment.findViewById(R.id.room);
        salonTable=fragment.findViewById(R.id.salontable);
        tablePlace=fragment.findViewById(R.id.tablePlace);
        tvPlace=fragment.findViewById(R.id.tvPlace);
        couch=fragment.findViewById(R.id.couch);


        viewModel.getFurniture().observe(this,list -> {

            for(Furniture furniture : list){

                Uri uri;
                switch (furniture.getFournitureType()){

                    case "table" :
                        uri = Uri.parse(furniture.getUrl());
                        tablePlace.setImageURI(uri);
                        break;

                    case "tv"    :
                        uri = Uri.parse(furniture.getUrl());
                        tvPlace.setImageURI(uri);
                        break;

                    case "chair" :
                        uri = Uri.parse(furniture.getUrl());
                        tvPlace.setImageURI(uri);
                        break;

                    case "computer" :
                        uri = Uri.parse(furniture.getUrl());
                        tablePlace.setImageURI(uri);
                        break;


                    case "couch" :
                        uri = Uri.parse(furniture.getUrl());
                        couch.setImageURI(uri);
                        break;

                    case "salonTable" :
                        uri = Uri.parse(furniture.getUrl());
                        salonTable.setImageURI(uri);
                        break;
                    default: ;


                }
            }
        });


        return fragment;
    }


    public interface homeShow {
         void onHomeShow(String url);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            monStartWork=(homeShow)context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+" must implement homeShow interface");
        }
    }
}

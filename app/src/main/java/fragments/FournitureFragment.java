package fragments;


import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import arrayAdapters.FournitureGridAdapter;
import database.Furniture;
import viewmodels.ViewModelFourHome;


public class FournitureFragment extends Fragment {


    private  OnFournitureClicked monFournitureClicked;

    private ViewModelFourHome viewModel;
    public FournitureFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        int slot = getArguments().getInt("slot");

        View fournitureFragment= inflater.inflate(R.layout.fragment_fourniture, container, false);


        viewModel=ViewModelProviders.of(getActivity()).get(ViewModelFourHome.class);

        List<Furniture> furnitures =MainMenu.myAppDataBase.myDao().getFurnitures();

        FournitureGridAdapter fournitureGridAdapter =new FournitureGridAdapter(getContext(),furnitures,slot);

        GridView gridFourniture =fournitureFragment.findViewById(R.id.gridFourniture);

        gridFourniture.setAdapter(fournitureGridAdapter);

        gridFourniture.setOnItemClickListener((parent, view, position, id) -> {

            Furniture fourniture =(Furniture)parent.getItemAtPosition(position);

            monFournitureClicked.deliverFourniture(fourniture);

            //viewModel.setFurniture(fourniture);

        });


        return fournitureFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monFournitureClicked=(OnFournitureClicked) context;

        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnFournitureClicked interface");
        }
    }

    public interface OnFournitureClicked{


        public void deliverFourniture(Furniture fourniture);
    }

}

package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.FournitureGridAdapter;
import beans.Furniture;


public class FournitureFragment extends Fragment {


    private  OnFournitureClicked monFournitureClicked;

    public FournitureFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View fournitureFragment= inflater.inflate(R.layout.fragment_fourniture, container, false);
/*

        ArrayList<Furniture> furnitures = Furniture.initFourniture(getContext());

        FournitureGridAdapter fournitureGridAdapter =new FournitureGridAdapter(getContext(), furnitures);

        GridView gridFourniture =fournitureFragment.findViewById(R.id.gridFourniture);

        gridFourniture.setAdapter(fournitureGridAdapter);

        gridFourniture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Furniture fourniture =(Furniture)parent.getItemAtPosition(position);

                monFournitureClicked.deliverFourniture(fourniture);


            }
        });

*/
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

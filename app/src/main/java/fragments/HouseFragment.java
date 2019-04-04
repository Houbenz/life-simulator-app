package fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.houbenz.lifesimulator.R;

import beans.House;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends Fragment {

    OnHouseClicked monHouseClicked;


    public HouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment= inflater.inflate(R.layout.fragment_house, container, false);

/*
        ArrayList<House>houses=House.initHouse(getContext());
        ListView houseView =fragment.findViewById(R.id.houseView);
        HouseListAdapter houseListAdapter =new HouseListAdapter(getContext(),houses);

        houseView.setAdapter(houseListAdapter);
        houseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                House house =(House)parent.getItemAtPosition(position);
                monHouseClicked.deliverHouse(house);
            }
        });

*/
        return fragment;

    }

    public interface OnHouseClicked {

        public void deliverHouse(House house);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            monHouseClicked=(OnHouseClicked)context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement OnHouseClicked interface");
        }
    }
}

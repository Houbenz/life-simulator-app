package fragments;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.List;

import arrayAdapters.CarListAdapter;
import database.Car;
import database.Degree;
import viewmodels.ViewModelCars;


public class CarFragment extends Fragment {


    ViewModelCars viewModelCars;

    public CarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_car, container, false);

        viewModelCars=ViewModelProviders.of(getActivity()).get(ViewModelCars.class);

        int slot = getArguments().getInt("slot");

        List<Car> cars = MainMenu.myAppDataBase.myDao().getCars();

        CarListAdapter carListAdapter = new CarListAdapter(getContext(),cars,slot);

        ListView carListView = fragment.findViewById(R.id.carListView);

        carListView.setAdapter(carListAdapter);

        carListView.setOnItemClickListener((parent, view, position, id) -> {

            Car car =(Car)parent.getItemAtPosition(position);


            viewModelCars.setCar(car);

        });

        return  fragment;
    }



}

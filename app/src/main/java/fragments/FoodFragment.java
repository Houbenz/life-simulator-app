package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import arrayAdapters.FoodListAdapter;
import database.Food;


public class FoodFragment extends Fragment {

    private onFoodClicked monFoodClicked;

    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_food, container, false);

        List<Food> foods = MainMenu.myAppDataBase.myDao().getFoods();

        FoodListAdapter foodListAdapter =new FoodListAdapter(getContext(),foods);

        ListView foodList =frameLayout.findViewById(R.id.foodList);

        foodList.setAdapter(foodListAdapter);

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Food food=(Food)parent.getItemAtPosition(position);
                monFoodClicked.deliverFood(food);

            }
        });



        return frameLayout;
    }

    public interface onFoodClicked{

        public void deliverFood(Food food);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monFoodClicked=(onFoodClicked)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement onFoodClicked interface");
        }
    }
}

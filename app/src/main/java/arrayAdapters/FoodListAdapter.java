package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.Locale;

import beans.Food;


public class FoodListAdapter extends ArrayAdapter<Food> {



    public FoodListAdapter(@NonNull Context context, ArrayList<Food> foods) {
        super(context, R.layout.food_res,foods);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

       @SuppressLint("viewHolder")
       View foodView =layoutInflater.inflate(R.layout.food_res,parent,false);

        Food food=getItem(position);

        TextView name =foodView.findViewById(R.id.name);
        TextView price=foodView.findViewById(R.id.price);
        TextView benefit=foodView.findViewById(R.id.benefit);
        TextView description=foodView.findViewById(R.id.description);
        ImageView foodImg = foodView.findViewById(R.id.foodImg);



            if(food != null) {

                String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)food.getPrice());
                String benefitString =String.format(Locale.ENGLISH,"%s : %d %s",getContext().getString(R.string.benefit),food.getBenefit(), getContext().getString(R.string.hunger));
                String descriptionString =String.format(Locale.ENGLISH,"%s : %s",getContext().getString(R.string.description),food.getDescription());

                name.setText(food.getName());
                price.setText(priceString);
                benefit.setText(benefitString);
                description.setText(descriptionString);

                Uri imgURI = Uri.parse(food.getImagePath());
                foodImg.setImageURI(imgURI);


            }
        return foodView;
    }
}

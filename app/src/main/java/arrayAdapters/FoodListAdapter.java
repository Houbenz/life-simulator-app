package arrayAdapters;

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

import beans.Food;


public class FoodListAdapter extends ArrayAdapter<Food> {



    public FoodListAdapter(@NonNull Context context, ArrayList<Food> foods) {
        super(context, R.layout.food_res,foods);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View foodView =layoutInflater.inflate(R.layout.food_res,parent,false);

        Food food=getItem(position);

        TextView name =foodView.findViewById(R.id.name);
        TextView price=foodView.findViewById(R.id.price);
        TextView benefit=foodView.findViewById(R.id.benefit);
        TextView description=foodView.findViewById(R.id.description);
        ImageView foodImg = foodView.findViewById(R.id.foodImg);


        name.setText(food.getName());
        price.setText("price : "+food.getPrice()+"$");
        benefit.setText("benefit : "+food.getBenefit());
        description.setText("description : "+food.getDescription());

        Uri imgURI=Uri.parse(food.getImagePath());
        foodImg.setImageURI(imgURI);


        return foodView;
    }
}

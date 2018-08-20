package arrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import beans.House;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class HouseListAdapter extends ArrayAdapter<House> {
    public HouseListAdapter(@NonNull Context context, ArrayList<House> houses) {
        super(context, R.layout.commun_buy_res,houses);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        View houseView =layoutInflater.inflate(R.layout.commun_buy_res,parent,false);

        House house =getItem(position);

       TextView name =houseView.findViewById(R.id.name);
        TextView price =houseView.findViewById(R.id.price);
        TextView benefit =houseView.findViewById(R.id.benefit);
        name.setText(house.getName());
        price.setText(house.getPrice()+"$");
        benefit.setText("Energy Bonus : +"+house.getBonusE()+"\tHealth Bonus : +"+house.getBonusH());

        return houseView;
    }
}


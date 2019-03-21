package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;

import java.util.ArrayList;
import java.util.Locale;

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

        @SuppressLint("viewHolder")
        View houseView =layoutInflater.inflate(R.layout.commun_buy_res,parent,false);

        House house =getItem(position);

       TextView name =houseView.findViewById(R.id.name);
        TextView price =houseView.findViewById(R.id.price);
        TextView benefit =houseView.findViewById(R.id.benefit);

        if(house != null) {
            String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)house.getPrice());
            String benefitString =String.format(Locale.ENGLISH,"%s : +%d\t %s : +%d %s",
                    getContext().getString(R.string.benefit),
                    house.getBonusE(),getContext().getString(R.string.energyBonus),
                    house.getBonusH(),getContext().getString(R.string.healthBonus));


            name.setText(house.getName());
            price.setText(priceString);
            benefit.setText(benefitString);
        }
        return houseView;
    }
}


package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.Acquired_Houses;
import database.House;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class HouseListAdapter extends ArrayAdapter<House> {




    private List<Acquired_Houses> acquired_houses;

    public HouseListAdapter(@NonNull Context context, List<House> houses,int slot) {
        super(context, R.layout.house_res,houses);
        acquired_houses=MainMenu.myAppDataBase.myDao().getAcquiredHouses(slot);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View houseView =layoutInflater.inflate(R.layout.house_res,parent,false);

        House house =getItem(position);

       TextView name =houseView.findViewById(R.id.name);
        TextView price =houseView.findViewById(R.id.price);
        TextView benefit =houseView.findViewById(R.id.benefit);
        ImageView comImage = houseView.findViewById(R.id.comImage);
        TextView owned = houseView.findViewById(R.id.owned);



        if(house != null) {

            boolean in = false ;


            for(Acquired_Houses acquired_houses : acquired_houses)
            {

                if(house.getId() == acquired_houses.getHouse_id()){
                    in=true;
                }
            }



            String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)house.getPrice());
            String benefitString =String.format(Locale.ENGLISH,"%s : +%d\t %s : +%d %s",
                    getContext().getString(R.string.benefit),
                    house.getBonusEnergy(),getContext().getString(R.string.energyBonus),
                    house.getBonusHealth(),getContext().getString(R.string.healthBonus));


            name.setText(house.getName());
            price.setText(priceString);
            benefit.setText(benefitString);
            comImage.setImageURI(Uri.parse(house.getImgUrl()));

            if(in){
                owned.setText("Yes");
                owned.setTextColor(getContext().getResources().getColor(R.color.green));
            }else{
                owned.setText("No");
                owned.setTextColor(getContext().getResources().getColor(R.color.red));
            }



        }
        return houseView;
    }
}


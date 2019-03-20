package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.Acquired_Furnitures;
import database.Furniture;

/**
 * Created by Houbenz on 01/08/2018.
 */

public class FournitureGridAdapter extends ArrayAdapter<Furniture> {

    private  int slot;
    public FournitureGridAdapter(@NonNull Context context, List<Furniture> fournitures,int slot) {


        super(context, R.layout.fourniture_res,fournitures);
        this.slot=slot;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View fournitureRes=layoutInflater.inflate(R.layout.fourniture_res,parent, false);



        Furniture fourniture =getItem(position);

        TextView name=fournitureRes.findViewById(R.id.fournitureName);
        TextView price=fournitureRes.findViewById(R.id.fourniturePrice);
        TextView fournitureType=fournitureRes.findViewById(R.id.fournitureType);
        ImageView fournitureImg = fournitureRes.findViewById(R.id.fournitureImg);

        List<Acquired_Furnitures> acquired_furnitures = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(slot);


        boolean in = false;

        for(Acquired_Furnitures acq : acquired_furnitures){
            if( acq.getFurn_id() == fourniture.getId())
                in=true;
        }

        Uri uri = Uri.parse(fourniture.getImgUrl());
        fournitureImg.setImageURI(uri);

        if(fourniture != null) {
            String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)fourniture.getPrice());
            String fournitureTypeString =String.format(Locale.ENGLISH,"%s : %s",getContext().getString(R.string.type),fourniture.getFurnitureType());

            if(in) {
                name.setText(fourniture.getName());
                name.setTextColor(getContext().getResources().getColor(R.color.green));
            }
            else {
                name.setText(fourniture.getName());
            }
            price.setText(priceString);
            fournitureType.setText(fournitureTypeString);

        }


        return fournitureRes;
    }
}

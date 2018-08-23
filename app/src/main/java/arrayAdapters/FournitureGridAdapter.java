package arrayAdapters;

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

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import beans.Furniture;

/**
 * Created by Houbenz on 01/08/2018.
 */

public class FournitureGridAdapter extends ArrayAdapter<Furniture> {

    public FournitureGridAdapter(@NonNull Context context, ArrayList<Furniture> fournitures) {


        super(context, R.layout.fourniture_res,fournitures);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        View fournitureRes=layoutInflater.inflate(R.layout.fourniture_res,parent, false);


        Furniture fourniture =getItem(position);

        TextView name=fournitureRes.findViewById(R.id.fournitureName);
        TextView price=fournitureRes.findViewById(R.id.fourniturePrice);
        TextView fournitureType=fournitureRes.findViewById(R.id.fournitureType);
        ImageView fournitureImg = fournitureRes.findViewById(R.id.fournitureImg);

        Uri uri =Uri.parse("android.resource://com.example.android.testsharedpreferences/drawable/ic_medium_tv");

        fournitureImg.setImageURI(uri);

        name.setText(fourniture.getName());
        price.setText("price : "+fourniture.getPrice()+"$");
        fournitureType.setText("type : "+fourniture.getFournitureType());



        return fournitureRes;
    }
}

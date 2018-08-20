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

import beans.Fourniture;

/**
 * Created by Houbenz on 01/08/2018.
 */

public class FournitureGridAdapter extends ArrayAdapter<Fourniture> {

    public FournitureGridAdapter(@NonNull Context context, ArrayList<Fourniture> fournitures) {


        super(context, R.layout.fourniture_res,fournitures);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        View fournitureRes=layoutInflater.inflate(R.layout.fourniture_res,parent, false);


        Fourniture fourniture =getItem(position);

        TextView name=fournitureRes.findViewById(R.id.fournitureName);
        TextView price=fournitureRes.findViewById(R.id.fourniturePrice);
        TextView fournitureType=fournitureRes.findViewById(R.id.fournitureType);

        name.setText(fourniture.getName());
        price.setText(fourniture.getPrice()+"$");
        fournitureType.setText(fourniture.getFournitureType());



        return fournitureRes;
    }
}

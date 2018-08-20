package arrayAdapters;

import android.content.Context;
import android.graphics.Color;
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

import beans.Buy;

/**
 * Created by Houbenz on 31/07/2018.
 */

public class BuyGridAdapter extends ArrayAdapter<Buy> {


    public BuyGridAdapter( Context context, ArrayList<Buy> buys) {
        super(context, R.layout.buy_res ,buys);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());

        View buyView =inflater.inflate(R.layout.buy_res,parent,false);


        Buy buy =getItem(position);

        TextView buyName = buyView.findViewById(R.id.buyName);
        ImageView buyImg = buyView.findViewById(R.id.buyImg);

        buyName.setText(buy.getName());

        buyView.setBackgroundColor(Color.parseColor(buy.getColor()));

        Uri imgURI = Uri.parse(buy.getImagePath());
        buyImg.setImageURI(imgURI);



        return buyView;
    }
}

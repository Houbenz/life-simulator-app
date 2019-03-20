package arrayAdapters;

import android.annotation.SuppressLint;
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

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.List;

import beans.Buy;
import database.MainFragments;

/**
 * Created by Houbenz on 31/07/2018.
 */

public class BuyGridAdapter extends ArrayAdapter<MainFragments> {



    public BuyGridAdapter( Context context,List<MainFragments> mainFragments) {
        super(context, R.layout.buy_res,mainFragments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());

       @SuppressLint("viewHolder")
       View buyView =inflater.inflate(R.layout.buy_res,parent,false);


        MainFragments mainFragments =getItem(position);

        TextView buyName = buyView.findViewById(R.id.buyName);
        ImageView buyImg = buyView.findViewById(R.id.buyImg);

        if(mainFragments != null) {
            buyName.setText(mainFragments.getName());


        buyView.setBackgroundColor(Color.parseColor(mainFragments.getColor()));

        Uri imgURI = Uri.parse(mainFragments.getImage_Uri());
        buyImg.setImageURI(imgURI);

        }

        return buyView;
    }
}

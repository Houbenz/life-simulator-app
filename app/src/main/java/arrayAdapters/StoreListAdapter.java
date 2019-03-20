package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.Acquired_Stores;
import database.Acquired_degree;
import database.Store;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class StoreListAdapter extends ArrayAdapter<Store> {



    private  List<Acquired_Stores> acquired_stores;
    public StoreListAdapter(@NonNull Context context, List<Store> stores, List<Acquired_Stores> acquired_stores) {
        super(context, R.layout.commun_buy_res, stores);

        this.acquired_stores =acquired_stores;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View storeView =layoutInflater.inflate(R.layout.commun_buy_res,parent,false);

        Store store =getItem(position);

        TextView name =storeView.findViewById(R.id.name);
        TextView price =storeView.findViewById(R.id.price);
        TextView benefit =storeView.findViewById(R.id.benefit);
        TextView owned =storeView.findViewById(R.id.owned);
        ImageView comImage=storeView.findViewById(R.id.comImage);

        if(store != null) {
            String benefitString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.incomePerDay),(int)store.getIncome());
            String priceBenefit= String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)store.getPrice());
            name.setText(store.getName());
            price.setText(priceBenefit);

            benefit.setText(benefitString);

            Uri uri = Uri.parse(store.getImgUrl());

            comImage.setImageURI(uri);

            boolean in = false;
            int i = 0;

            while (!in && i < acquired_stores.size()) {

                if (acquired_stores.get(i).getStore_id() == store.getId())
                    in = true;
                i++;
            }

            if (in) {
                owned.setText(getContext().getString(R.string.acquiredYes));
                owned.setTextColor(getContext().getResources().getColor(R.color.green));
                storeView.setClickable(true);

            } else {
                owned.setText(getContext().getString(R.string.acquiredNo));
                owned.setTextColor(getContext().getResources().getColor(R.color.red));
            }

        }


        return storeView;
    }
}

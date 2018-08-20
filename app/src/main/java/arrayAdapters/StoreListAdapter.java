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
import beans.Store;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class StoreListAdapter extends ArrayAdapter<Store> {

    public StoreListAdapter(@NonNull Context context, ArrayList<Store> stores) {
        super(context, R.layout.commun_buy_res,stores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= LayoutInflater.from(getContext());

        View storeView =layoutInflater.inflate(R.layout.commun_buy_res,parent,false);


        Store store =getItem(position);

        TextView name =storeView.findViewById(R.id.name);
        TextView price =storeView.findViewById(R.id.price);
        TextView benefit =storeView.findViewById(R.id.benefit);

        name.setText(store.getName());

        price.setText("Price : "+ store.getPrice()+"$");

        benefit.setText("Income : "+store.getIncome()+"$");


        return storeView;
    }
}

package arrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import beans.House;
import beans.Store;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class StoreListAdapter extends ArrayAdapter<Store> {



    private  ArrayList<String > stores;
    public StoreListAdapter(@NonNull Context context, ArrayList<Store> stores,ArrayList<String> storesS) {
        super(context, R.layout.commun_buy_res,stores);

        this.stores=storesS;
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
        TextView owned =storeView.findViewById(R.id.owned);


        name.setText(store.getName());
        price.setText("Price : "+ store.getPrice()+"$");
        benefit.setText("Income per day : "+store.getIncome()+"$");

        boolean in =false;
        int i=0;

        while(!in && i<stores.size()){

            if(stores.get(i).equals(store.getName()))
            in=true;

            Log.i("LOKIA",stores.get(i));

            i++;
        }

        if(in){
            owned.setText("Owned : Yes.");
            owned.setTextColor(getContext().getResources().getColor(R.color.green));
            storeView.setClickable(true);

        }else{
            owned.setText("Owned : No.");
            owned.setTextColor(getContext().getResources().getColor(R.color.red));
        }



        return storeView;
    }
}

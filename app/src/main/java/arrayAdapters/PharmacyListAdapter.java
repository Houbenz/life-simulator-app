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

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.Locale;

import beans.Medicine;


public class PharmacyListAdapter extends ArrayAdapter<Medicine> {

    public PharmacyListAdapter(@NonNull Context context, ArrayList<Medicine> medicines) {
        super(context, R.layout.commun_buy_res,medicines);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater =LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View medicineView =layoutInflater.inflate(R.layout.commun_buy_res,parent,false);

        Medicine medicine=getItem(position);

        TextView name =medicineView.findViewById(R.id.name);
        TextView price =medicineView.findViewById(R.id.price);
        TextView benefit =medicineView.findViewById(R.id.benefit);
        TextView owned =medicineView.findViewById(R.id.owned);

        if(medicine != null) {
            String priceString =String.format(Locale.ENGLISH,"%d$",(int)medicine.getPrice());
            String benefitString =String.format(Locale.ENGLISH,"%s : %d %s",
                    getContext().getString(R.string.benefit),medicine.getBenefit(),getContext().getString(R.string.health));

            name.setText(medicine.getName());
            owned.setText("");
            price.setText(priceString);
            benefit.setText(benefitString);


        }
        return medicineView;
    }
}

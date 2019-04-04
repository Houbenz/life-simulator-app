package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;

import java.util.List;
import java.util.Locale;

import database.Medicine;


public class PharmacyListAdapter extends ArrayAdapter<Medicine> {

    public PharmacyListAdapter(@NonNull Context context, List<Medicine> medicines) {
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
        ImageView comImage = medicineView.findViewById(R.id.comImage);

        if(medicine != null) {
            String priceString =String.format(Locale.ENGLISH,"%d$",(int)medicine.getPrice());
            String benefitString =String.format(Locale.ENGLISH,"%s : %d %s",
                    getContext().getString(R.string.benefit),medicine.getBenefit(),getContext().getString(R.string.health));

            name.setText(medicine.getName());
            owned.setText("");
            price.setText(priceString);
            benefit.setText(benefitString);
            comImage.setImageURI(Uri.parse(medicine.getImgUrl()));


        }
        return medicineView;
    }
}

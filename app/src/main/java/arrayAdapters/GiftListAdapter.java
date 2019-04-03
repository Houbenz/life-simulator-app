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

import com.android.houbenz.lifesimulator.R;

import java.util.List;

import database.Gift;

public class GiftListAdapter extends ArrayAdapter<Gift> {

    public GiftListAdapter(@NonNull Context context, List<Gift> gifts) {
        super(context, R.layout.commun_buy_res,gifts);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View giftView=inflater.inflate(R.layout.commun_buy_res,parent,false);

        Gift gift = getItem(position);

        TextView name=giftView.findViewById(R.id.name);
        TextView price=giftView.findViewById(R.id.price);
        ImageView image=giftView.findViewById(R.id.comImage);

        name.setText(gift.getName());
        price.setText(gift.getPrice()+"$");
        image.setImageURI(Uri.parse(gift.getImgUrl()));

        return giftView;
    }
}

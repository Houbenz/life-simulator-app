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
import com.houbenz.lifesimulator.MainMenu;

import java.util.List;

import conf.Params;
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
        TextView benefit=giftView.findViewById(R.id.benefit);
        TextView price=giftView.findViewById(R.id.price);
        TextView owned=giftView.findViewById(R.id.owned);
        ImageView image=giftView.findViewById(R.id.comImage);

        name.setText(gift.getName());
        price.setText(gift.getPrice()+"$");
        image.setImageURI(Uri.parse(gift.getImgUrl()));

        benefit.setTextColor(getContext().getResources().getColor(R.color.green));

        Gift gift1=null;

        switch (gift.getId()){
            case 2:
                 gift1 = MainMenu.myAppDataBase.myDao().getRoses();
                owned.setText(getContext().getString(R.string.own)+" :"+gift1.getGiftCount()+ " "+gift.getName());
                benefit.setText(getContext().getString(R.string.benefit)+" : +"+Params.ROSES_BONUS+" "+getContext().getString(R.string.to_relationship));
                break;

            case 1:
                 gift1 = MainMenu.myAppDataBase.myDao().getChocolate();
                owned.setText(getContext().getString(R.string.own)+" :"+gift1.getGiftCount()+ " "+gift.getName());
                benefit.setText(getContext().getString(R.string.benefit)+" : +"+Params.CHOCOLATE_BONUS+" "+getContext().getString(R.string.to_relationship));

                break;

            case 3:
                 gift1 = MainMenu.myAppDataBase.myDao().getJewelry();
                owned.setText(getContext().getString(R.string.own)+" :"+gift1.getGiftCount()+ " "+gift.getName());
                benefit.setText(getContext().getString(R.string.benefit)+" : +"+Params.JEWELRY_BONUS+" "+getContext().getString(R.string.to_relationship));
                break;
        }

        return giftView;
    }
}

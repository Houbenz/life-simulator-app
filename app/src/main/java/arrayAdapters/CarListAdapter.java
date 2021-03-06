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

import database.Acquired_Cars;
import database.Car;

public class CarListAdapter extends ArrayAdapter<Car> {

    private int slot;
    public CarListAdapter(@NonNull Context context, List<Car> cars,int slot) {
        super(context, R.layout.commun_buy_res,cars);
        this.slot=slot;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());


        Car car = getItem(position);

        @SuppressLint("ViewHolder")
        View view =inflater.inflate(R.layout.commun_buy_res,parent,false);

        ImageView image =view.findViewById(R.id.comImage);
        TextView name=view.findViewById(R.id.name);
        TextView price=view.findViewById(R.id.price);
        TextView benefit=view.findViewById(R.id.benefit);
        TextView owned=view.findViewById(R.id.owned);

       List<Acquired_Cars> acquired_cars=MainMenu.myAppDataBase.myDao().getAcquiredCars(slot);


       boolean in = false;

       for(Acquired_Cars acq : acquired_cars)

       {
           if(car.getId() == acq.getCar_id()){
               in = true;
           }
       }

        if(car != null){
           
            image.setImageURI(Uri.parse(car.getImgUrl()));
            name.setText(car.getName());
            price.setText(car.getPrice()+"$");
            benefit.setText(getContext().getString(R.string.none));
            if(in){
                owned.setText(getContext().getString(R.string.owned_yes));
                owned.setTextColor(getContext().getResources().getColor(R.color.green));
            }else{
                owned.setText(getContext().getString(R.string.owned_no));
                owned.setTextColor(getContext().getResources().getColor(R.color.red));
            }

        }

        return view;
    }
}

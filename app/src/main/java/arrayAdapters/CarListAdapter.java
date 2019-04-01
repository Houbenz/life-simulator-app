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

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.List;

import database.Acquired_Cars;
import database.Car;
import database.MainFragments;

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
            benefit.setText("None");
            if(in){
                owned.setText("Yes");
                owned.setTextColor(getContext().getResources().getColor(R.color.green));
            }else{
                owned.setText("No");
                owned.setTextColor(getContext().getResources().getColor(R.color.red));
            }

        }

        return view;
    }
}

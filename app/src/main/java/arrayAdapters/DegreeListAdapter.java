package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.Acquired_degree;
import database.Degree;

/**
 * Created by Houbenz on 20/08/2018.
 */

public class DegreeListAdapter extends ArrayAdapter<Degree> {


    private  List<Acquired_degree> acquiredDegrees;
    public DegreeListAdapter(@NonNull Context context, List<Degree> degrees) {
        super(context, R.layout.learn_res,degrees);
        this.acquiredDegrees=MainMenu.myAppDataBase.myDao().getAcquiredDegrees();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= LayoutInflater.from(getContext());

        @SuppressLint("viewHolder")
        View learnView = layoutInflater.inflate(R.layout.learn_res,parent,false);

        //Learn learn=getItem(position);
           Degree degree = getItem(position) ;

        TextView name=learnView.findViewById(R.id.nameLearn);
        TextView price=learnView.findViewById(R.id.priceLearn);
        TextView acquired=learnView.findViewById(R.id.acqLearn);

            if(degree != null) {

                String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)degree.getPrice());
                name.setText(degree.getName());
                price.setText(priceString);


                boolean in = false;
                int i = 0;

                while (!in && i < acquiredDegrees.size()) {

                    if (acquiredDegrees.get(i).getDegree_id() == degree.getId())
                        in = true;
                    i++;

                }


                if (in) {
                    acquired.setText(getContext().getString(R.string.acquiredYes));
                    acquired.setTextColor(getContext().getResources().getColor(R.color.green));
                    learnView.setClickable(true);
                } else {

                    acquired.setText(getContext().getString(R.string.acquiredNo));
                    acquired.setTextColor(getContext().getResources().getColor(R.color.red));
                }

            }
        return  learnView;
    }
}

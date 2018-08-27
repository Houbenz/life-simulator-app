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

import java.util.ArrayList;

import beans.Learn;

/**
 * Created by Houbenz on 20/08/2018.
 */

public class LearnListAdapter extends ArrayAdapter<Learn> {


    private  ArrayList<String> acquiredDegrees;
    public LearnListAdapter(@NonNull Context context, ArrayList<Learn> learns,ArrayList<String > acquiredDegrees) {
        super(context, R.layout.learn_res,learns);
        this.acquiredDegrees=acquiredDegrees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= LayoutInflater.from(getContext());

        View learnView = layoutInflater.inflate(R.layout.learn_res,parent,false);

        Learn learn=getItem(position);

        TextView name=learnView.findViewById(R.id.nameLearn);
        TextView price=learnView.findViewById(R.id.priceLearn);
        TextView acquired=learnView.findViewById(R.id.acqLearn);


        name.setText(learn.getName());
        price.setText("price : "+learn.getPrice()+"$");


        boolean in = false;
        int i=0;

        while(!in && i<acquiredDegrees.size()){

            if(acquiredDegrees.get(i).equals(learn.getName()))
                in=true;
            i++;

        }


        if(in) {
            acquired.setText("acquired : Yes");
            acquired.setTextColor(getContext().getResources().getColor(R.color.green));
            learnView.setClickable(true);
        }
        else{

            acquired.setText("acquired : No");
            acquired.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        return  learnView;
    }
}

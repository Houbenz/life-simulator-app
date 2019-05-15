package arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;
import java.util.Locale;

import database.Acquired_degree;
import database.Degree;

/**
 * Created by Houbenz on 20/08/2018.
 */

public class DegreeListAdapter extends ArrayAdapter<Degree> {


    private int slot;
    private  List<Acquired_degree> acquiredDegrees;
    public DegreeListAdapter(@NonNull Context context, List<Degree> degrees, int slot) {
        super(context, R.layout.learn_res,degrees);
        this.acquiredDegrees=MainMenu.myAppDataBase.myDao().getAcquiredDegrees(slot);
        this.slot=slot;
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
        ProgressBar progressStudy=learnView.findViewById(R.id.progressStudy);
        TextView textprog = learnView.findViewById(R.id.textprogress);
        ImageView degreeImage = learnView.findViewById(R.id.degreeImg);

            if(degree != null) {

                String priceString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.price),(int)degree.getPrice());
                name.setText(degree.getName());
                price.setText(priceString);
                degreeImage.setImageURI(Uri.parse(degree.getImgUrl()));


                Acquired_degree acq = MainMenu.myAppDataBase.myDao().getAcqDegr(slot,degree.getId());

                progressStudy.setMax(degree.getProgress());
                textprog.setText(progressStudy.getProgress() + "/"+progressStudy.getMax());



               if(acq == null){

                   textprog.setText(progressStudy.getProgress() + "/"+progressStudy.getMax());
                   acquired.setText(getContext().getString(R.string.acquiredNo));
                   acquired.setTextColor(getContext().getResources().getColor(R.color.red));

               }else {
                   if(acq.getPlayer_progress() >= progressStudy.getMax()){

                       acquired.setText(getContext().getString(R.string.acquiredYes));
                       acquired.setTextColor(getContext().getResources().getColor(R.color.green));
                       progressStudy.setProgress(acq.getPlayer_progress());
                       learnView.setClickable(true);
                   }else{

                       acquired.setTextColor(getContext().getResources().getColor(R.color.red));
                   }

                   textprog.setText(acq.getPlayer_progress() + "/"+progressStudy.getMax());
                   progressStudy.setProgress(acq.getPlayer_progress());
               }

/*
                boolean in = false;
                int i = 0;

                while (!in && i < acquiredDegrees.size()) {

                    if (acquiredDegrees.get(i).getDegree_id() == degree.getId()) {
                        in = true;
                        progressStudy.setProgress(progressStudy.getProgress() + acquiredDegrees.get(i).getPlayer_progress());
                        break;
                    }
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
*/
            }
        return  learnView;
    }
}

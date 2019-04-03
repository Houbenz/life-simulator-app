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

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;
import java.util.Locale;

import database.Acquired_degree;
import database.Work;

public class WorksGridAdapter extends ArrayAdapter<Work> {


    private int level ;
    private int slot ;
    private  List<Acquired_degree> acquiredDegrees;
    public WorksGridAdapter(@NonNull Context context, List<Work> works, int level ,int slot){
        super(context, R.layout.work_res,works);
        this.level=level;
        acquiredDegrees=MainMenu.myAppDataBase.myDao().getAcquiredDegrees(slot);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {




        Work work =getItem(position);
        LayoutInflater inflater =LayoutInflater.from(getContext());

        @SuppressLint("ViewHolder")
        View workView =inflater.inflate(R.layout.work_res,parent,false);

        TextView workName=workView.findViewById(R.id.workName);
        TextView workPay=workView.findViewById(R.id.workpay);
        TextView workTime=workView.findViewById(R.id.workTime);
        TextView workLevel =workView.findViewById(R.id.workLevel);
        TextView reqDegree =workView.findViewById(R.id.reqDegree);
        ImageView imageView = (ImageView)workView.findViewById(R.id.imageView);


        if(work != null) {

            String workTimeString =String.format(Locale.ENGLISH,"%s : %d hrs",getContext().getString(R.string.workTime),work.getWork_time());
            String payString =String.format(Locale.ENGLISH,"%s : %d$",getContext().getString(R.string.pay),(int)work.getIncome());
            String worklvlString =String.format(Locale.ENGLISH,"%s : %s %d",getContext().getString(R.string.required),getContext().getString(R.string.level),(int)work.getLvlToWork());
            String reqDegreeString = String.format(Locale.ENGLISH,"%s : %s ",getContext().getString(R.string.required),work.getDegree_required());

            workName.setText(work.getName());
            workPay.setText(payString);
            workTime.setText(workTimeString);
            workLevel.setText(worklvlString);
            reqDegree.setText(reqDegreeString);


        Uri imageURI =Uri.parse(work.getImgPath());

        imageView.setImageURI(imageURI);


        boolean in =false ;
        int i=0;


        while (!in && i<acquiredDegrees.size()) {

            if (acquiredDegrees.get(i).getDegree_Name().equals(work.getDegree_required())
                    && acquiredDegrees.get(i).getAvailable().equals("true"))
            in = true;

            i++;
        }

        if(work.getDegree_required().equals(getContext().getString(R.string.none)))
            in=true;


        if(work.getLvlToWork()>level && !in){
            reqDegree.setTextColor(getContext().getResources().getColor(R.color.red));
            workLevel.setTextColor(getContext().getResources().getColor(R.color.red));
            workView.setClickable(true);

        }else{
            if(work.getLvlToWork()>level && in){
                reqDegree.setTextColor(getContext().getResources().getColor(R.color.green));
                workLevel.setTextColor(getContext().getResources().getColor(R.color.red));
                workView.setClickable(true);
            }else{
                if(work.getLvlToWork()<=level && !in){

                    reqDegree.setTextColor(getContext().getResources().getColor(R.color.red));
                    workLevel.setTextColor(getContext().getResources().getColor(R.color.green));
                    workView.setClickable(true);
                }else {

                    reqDegree.setTextColor(getContext().getResources().getColor(R.color.green));
                    workLevel.setTextColor(getContext().getResources().getColor(R.color.green));
                    workView.setClickable(false);
                }
            }
        }
        }
            return workView;

    }
}

package arrayAdapters;

import android.content.ContentResolver;
import android.content.Context;
import android.media.Image;
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

import com.example.android.testsharedpreferences.R;

import java.net.URI;
import java.util.ArrayList;

import beans.Work;

public class WorksGridAdapter extends ArrayAdapter<Work> {


    private int level ;
    private  ArrayList<String > acquiredDegrees;
    public WorksGridAdapter(@NonNull Context context, ArrayList<Work> works, int level,ArrayList<String > acquiredDegrees){
        super(context, R.layout.work_res,works);
        this.level=level;
        this.acquiredDegrees=acquiredDegrees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {




        Work work =getItem(position);
        LayoutInflater inflater =LayoutInflater.from(getContext());

        View workView =inflater.inflate(R.layout.work_res,parent,false);

        TextView workName=workView.findViewById(R.id.workName);
        TextView workPay=workView.findViewById(R.id.workpay);
        TextView workTime=workView.findViewById(R.id.workTime);
        TextView workLevel =workView.findViewById(R.id.workLevel);
        TextView reqDegree =workView.findViewById(R.id.reqDegree);
        ImageView imageView = (ImageView)workView.findViewById(R.id.imageView);

        workName.setText(work.getName());
        workPay.setText("pay : "+work.getPay()+"$");
        workTime.setText("work time :"+work.getTimeOfWork()+"hrs");
        workLevel.setText("Required Level : "+work.getLeveltoWork());
        reqDegree.setText("Required : "+work.getReqDegree());

        Uri imageURI =Uri.parse(work.getImagePath());

        imageView.setImageURI(imageURI);


        boolean in =false ;
        int i=0;

        while (!in && i<acquiredDegrees.size()){

            if(acquiredDegrees.get(i).equals(work.getReqDegree()))
                in=true;
            Log.i("LOKIA",acquiredDegrees.get(i));
            i++;
        }


        if(work.getLeveltoWork()>level && !in){
            reqDegree.setTextColor(getContext().getResources().getColor(R.color.red));
            workLevel.setTextColor(getContext().getResources().getColor(R.color.red));
            workView.setClickable(true);

        }else{
            if(work.getLeveltoWork()>level && in){
                reqDegree.setTextColor(getContext().getResources().getColor(R.color.green));
                workLevel.setTextColor(getContext().getResources().getColor(R.color.red));
                workView.setClickable(true);
            }else{
                if(work.getLeveltoWork()<=level && !in){

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

            return workView;

    }
}

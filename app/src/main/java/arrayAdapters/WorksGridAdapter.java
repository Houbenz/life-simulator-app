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
    public WorksGridAdapter(@NonNull Context context, ArrayList<Work> works, int level){
        super(context, R.layout.work_res,works);
        this.level=level;
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
        ImageView imageView = (ImageView)workView.findViewById(R.id.imageView);

        workName.setText(work.getName());
        workPay.setText("pay : "+work.getPay()+"$");
        workTime.setText("work time :"+work.getTimeOfWork()+"hrs");
        workLevel.setText("Required Level : "+work.getLeveltoWork());

        Uri imageURI =Uri.parse(work.getImagePath());

        imageView.setImageURI(imageURI);

        if(work.getLeveltoWork()>level) {
            workLevel.setTextColor(getContext().getResources().getColor(R.color.red));
            workView.setClickable(true);
        }
        else {
            workLevel.setTextColor(getContext().getResources().getColor(R.color.green));
            workView.setClickable(false);
        }


            return workView;

    }
}

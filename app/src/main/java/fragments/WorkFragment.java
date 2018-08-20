package fragments;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.WorksGridAdapter;
import beans.Work;


public class WorkFragment extends Fragment {


    onWorkSelected mWorkSelected;

    public WorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout= (FrameLayout)inflater.inflate(R.layout.fragment_work, container, false);

        ArrayList<Work> works = instantiateWorks();



        //The player Level (IMPORTANT)
        int playerLevel = getArguments().getInt("playerLevel");


        WorksGridAdapter worksGridAdapter = new WorksGridAdapter(getContext(),works,playerLevel);
        Log.i("aze","the level : "+playerLevel);
        GridView gridView =frameLayout.findViewById(R.id.workGrid);

        gridView.setAdapter(worksGridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Work work = (Work) parent.getItemAtPosition(position);

                Toast.makeText( getContext(),"you're now a "+work.getName()+"", Toast.LENGTH_SHORT).show();

                mWorkSelected.deliverWork(work);

            }
        });

        return frameLayout;

    }


    public interface onWorkSelected
    {
        public void deliverWork( Work work);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mWorkSelected=(onWorkSelected)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement onWorkSelected interface");
        }



    }

    public ArrayList<Work> instantiateWorks(){

         return Work.workInit(getContext());

    }


}

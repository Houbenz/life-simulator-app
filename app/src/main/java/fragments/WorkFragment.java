package fragments;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.List;


import arrayAdapters.WorksGridAdapter;
import database.Acquired_degree;
import database.Degree;
import database.Work;


public class WorkFragment extends Fragment {


    onWorkSelected mWorkSelected;

    public WorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout= (FrameLayout)inflater.inflate(R.layout.fragment_work, container, false);

        List<Work> works = MainMenu.myAppDataBase.myDao().getWorks();

        //The player Level (IMPORTANT)
        int playerLevel = getArguments().getInt("playerLevel");


        WorksGridAdapter worksGridAdapter = new WorksGridAdapter(getContext(),works,playerLevel);
        GridView gridView =frameLayout.findViewById(R.id.workGrid);

        gridView.setAdapter(worksGridAdapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {

            Work work = (Work) parent.getItemAtPosition(position);

            Toast.makeText( getContext(),"you're now a "+work.getName()+"", Toast.LENGTH_SHORT).show();

            mWorkSelected.deliverWork(work);

        });

        return frameLayout;

    }


    public interface onWorkSelected
    {
         void deliverWork( Work work);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mWorkSelected = (onWorkSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onWorkSelected interface");
        }

    }

}

package fragments;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.Collections;

import arrayAdapters.WorksGridAdapter;
import database.Work;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;


public class WorkFragment extends Fragment {


    onWorkSelected mWorkSelected;
    private GridView gridView;
    private SharedPreferences sharedPreferences;
    public WorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout= (FrameLayout)inflater.inflate(R.layout.fragment_work, container, false);

        sharedPreferences=getContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);

        ArrayList<Work> works =(ArrayList<Work>) MainMenu.myAppDataBase.myDao().getWorks();
        Collections.sort(works);
        //The player Level (IMPORTANT)
        int playerLevel = getArguments().getInt("playerLevel");
        int slot = getArguments().getInt("slot");

        WorksGridAdapter worksGridAdapter = new WorksGridAdapter(getContext(),works,playerLevel,slot);
         gridView =frameLayout.findViewById(R.id.workGrid);

        gridView.setAdapter(worksGridAdapter);


        String firstTime=sharedPreferences.getString("firstTime","none");

        gridView.setOnItemClickListener((parent, view, position, id) -> {

            Work work = (Work) parent.getItemAtPosition(position);




            mWorkSelected.deliverWork(work);

        });

        if(firstTime.equals("none")) {
            showTuto(getString(R.string.first_time), getString(R.string.work_tuto_message),0);
        }
        return frameLayout;

    }


    public interface onWorkSelected
    {
         void deliverWork( Work work);
    }

    private void showTuto(String title,String contentText,int type){
        new GuideView.Builder(getActivity())
                .setTitle(title)
                .setTargetView(gridView)
                .setContentText(contentText)
                .setDismissType(DismissType.outside)
                .setGuideListener(view -> {
                    if(type == 0)
                    showTuto(getString(R.string.first_time), getString(R.string.click_work_worker),1);
                })
                .build()
                .show();
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

package fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import arrayAdapters.DegreeListAdapter;
import database.Degree;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DegreeFragment extends Fragment {

    private OnDegreeClick monDegreeClick;
    private ListView learnList;
    private SharedPreferences sharedPreferences;
    private int itemPosition;
    private int y ;



    public DegreeFragment(){

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_degree, container, false);


        sharedPreferences=getContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);

        int slot = getArguments().getInt("slot");

         learnList =fragment.findViewById(R.id.learnView);

        List<Degree> degrees = MainMenu.myAppDataBase.myDao().getDegrees();

        DegreeListAdapter degreeListAdapter =new DegreeListAdapter(getContext(),degrees,slot);

        learnList.setAdapter(degreeListAdapter);

        learnList.setSelectionFromTop(itemPosition,y);

        learnList.setOnItemClickListener((parent, view, position, id) -> {

            Degree degree =(Degree) parent.getItemAtPosition(position);

            View v = learnList.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - learnList.getPaddingTop());
            monDegreeClick.deliverDegree(degree);

            monDegreeClick.deliverDegreeItemPosition(learnList.getFirstVisiblePosition(),top);
        });


        String firstTime=sharedPreferences.getString("firstTimeLearn","none");

        if(firstTime.equals("none")) {
            showTuto("Learning", "When the degree reaches the max, the player owns it", 0);
            sharedPreferences.edit().putString("firstTimeLearn","finished").apply();
        }
            return fragment;
    }

    private void showTuto(String title,String contentText,int type){
        new GuideView.Builder(getContext())
                .setTitle(title)
                .setTargetView(learnList)
                .setContentText(contentText)
                .setDismissType(DismissType.outside)
                .setGuideListener(view ->{
                    if(type==0)
                    showTuto("Learning","click on Highschool degree to begin learning",1);
                })
                .build()
                .show();
    }

    public interface OnDegreeClick {

         void deliverDegree(Degree degree);
         void deliverDegreeItemPosition(int pos , int y );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monDegreeClick =(OnDegreeClick)context;
        }catch(ClassCastException e){
            throw  new ClassCastException(context.toString()+" must implement OnDegreeClick interface");
        }

    }


    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

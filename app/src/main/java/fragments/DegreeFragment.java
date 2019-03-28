package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import arrayAdapters.DegreeListAdapter;
import database.Degree;

/**
 * A simple {@link Fragment} subclass.
 */
public class DegreeFragment extends Fragment {

    private OnDegreeClick monDegreeClick;

    private int itemPosition;
    private int y ;



    public DegreeFragment(){

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_degree, container, false);


        int slot = getArguments().getInt("slot");

        ListView learnList =fragment.findViewById(R.id.learnView);

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

            return fragment;
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

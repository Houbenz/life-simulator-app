package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.List;

import arrayAdapters.DegreeListAdapter;
import database.Degree;

/**
 * A simple {@link Fragment} subclass.
 */
public class DegreeFragment extends Fragment {

    private OnDegreeClick monDegreeClick;

    public DegreeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_degree, container, false);

        ListView learnList =fragment.findViewById(R.id.learnView);

        List<Degree> degrees = MainMenu.myAppDataBase.myDao().getDegrees();

        DegreeListAdapter degreeListAdapter =new DegreeListAdapter(getContext(),degrees);

        learnList.setAdapter(degreeListAdapter);

        learnList.setOnItemClickListener((parent, view, position, id) -> {

            Degree degree =(Degree) parent.getItemAtPosition(position);

            monDegreeClick.deliverDegree(degree);

        });

            return fragment;
    }


    public interface OnDegreeClick {

         void deliverDegree(Degree degree);
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
}

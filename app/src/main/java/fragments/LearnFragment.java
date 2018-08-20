package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.LearnListAdapter;
import beans.Learn;

/**
 * A simple {@link Fragment} subclass.
 */
public class LearnFragment extends Fragment {

    private OnLearnClick monLearnClick;
    private ArrayList<String> acquiredDegress;

    public LearnFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_learn, container, false);

        ListView learnList =fragment.findViewById(R.id.learnView);

        acquiredDegress=getArguments().getStringArrayList("arr");

        for(int i=0;i<acquiredDegress.size();i++){
            Log.i("LOKIA",acquiredDegress.get(i));
        }




        ArrayList<Learn> learns =Learn.initLearn(getContext());
        LearnListAdapter learnListAdapter =new LearnListAdapter(getContext(),learns,acquiredDegress);

        learnList.setAdapter(learnListAdapter);

        learnList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Learn learn =(Learn)parent.getItemAtPosition(position);

                monLearnClick.deliverLearn(learn);

            }
        });

            return fragment;
    }


    public interface OnLearnClick{

        public void deliverLearn(Learn learn);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monLearnClick=(OnLearnClick)context;
        }catch(ClassCastException e){
            throw  new ClassCastException(context.toString()+" must implement OnLearnClick interface");
        }

    }
}

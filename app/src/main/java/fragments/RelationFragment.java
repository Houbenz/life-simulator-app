package fragments;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.HomeActivity;

import viewmodels.ViewModelPartner;


public class RelationFragment extends Fragment {


    public RelationFragment() {

    }

    private ViewModelPartner viewmodel;
    private Button lookPartner;
    private int dis =2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_relation, container, false);

        viewmodel = ViewModelProviders.of(getActivity()).get(ViewModelPartner.class);

        viewmodel.setIsLooking(false);

        lookPartner=fragment.findViewById(R.id.lookPartner);

        lookPartner.setOnClickListener(view ->{

            if(dis % 2 != 0) {
                viewmodel.setIsLooking(false);
                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                dis++;
            }else {
                viewmodel.setIsLooking(true);
                lookPartner.setText("Stop looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.red));
                dis++;
            }



        });


        return fragment;
    }

}

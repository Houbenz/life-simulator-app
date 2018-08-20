package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {

    OnStartWork monStartWork;

    private TextView intro;
    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        monStartWork.startDecHour();



        View fragment =inflater.inflate(R.layout.fragment_intro, container, false);

        intro=fragment.findViewById(R.id.intro);
        return fragment;
    }


    public interface OnStartWork{
        public void startDecHour();
    }


    public void setTextView(int hour){
        intro.setText("Time Left : "+hour+" hours");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            monStartWork=(OnStartWork)context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+" must implement OnStartWork interface");
        }
    }
}

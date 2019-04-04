package fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepFragment extends Fragment {

    private  onSleepClicked monSleepClicked;
    private int hoursNumber;

    public SleepFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View sleepFragment = inflater.inflate(R.layout.fragment_sleep, container, false);




        SeekBar sleepSeekBar =sleepFragment.findViewById(R.id.sleepSeekBar);
        sleepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                TextView selectedTime =sleepFragment.findViewById(R.id.selectedTime);
                selectedTime.setText(progress+"H");
                hoursNumber=progress;

            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        Button confirmSleep= sleepFragment.findViewById(R.id.confirmSleep);

        confirmSleep.setOnClickListener(v -> monSleepClicked.deliverSleep(hoursNumber));

        return sleepFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monSleepClicked=(onSleepClicked)context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement onSleepClicked interface");
        }
    }

    public interface onSleepClicked{
        public void deliverSleep(int hoursNumber);
    }

}

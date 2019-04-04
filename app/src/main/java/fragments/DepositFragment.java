package fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import database.Player;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {



    private Button deposit ;
    private SeekBar seekBarDeposit;
    private TextView seekMuchD;
    private TextView bankBalanceD;

    private OnDeposit monDeposit;

    public DepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_deposit, container, false);


        seekBarDeposit=frameLayout.findViewById(R.id.seekBarDeposit);
        deposit=frameLayout.findViewById(R.id.deposit);
        seekMuchD=frameLayout.findViewById(R.id.seekbarMuchD);
        bankBalanceD=frameLayout.findViewById(R.id.bankBalanceD);


        int slot =getArguments().getInt("slot");
        Player player = MainMenu.myAppDataBase.myDao().getPlayer(slot);
        double balance= player.getBalance();
        double balanceInBank=player.getBank_deposit();


        bankBalanceD.setText("Balance : "+balanceInBank+"$");

        seekBarDeposit.setMax((int)balance);


        seekBarDeposit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                seekMuchD.setText(seekBar.getProgress()+"$");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        deposit.setOnClickListener(v -> monDeposit.deliverDeposit(seekBarDeposit.getProgress()));


        return  frameLayout;
    }

    public interface OnDeposit{
        public void deliverDeposit(double deposit);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            monDeposit=(OnDeposit)context;

        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnDeposit interface");

        }
    }
}

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


public class WithdrawFragment extends Fragment {




    private SeekBar seekBarWithdraw;
    private Button withdraw;
    private TextView seekBarMuch;
    private TextView bankBalanceW;

    private OnWithdraw monWithdraw;

    public WithdrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout= (FrameLayout) inflater.inflate(R.layout.fragment_withdraw, container, false);


        seekBarMuch=frameLayout.findViewById(R.id.seekbarMuch);
        seekBarWithdraw=frameLayout.findViewById(R.id.seekBarDeposit);
        withdraw=frameLayout.findViewById(R.id.withdraw);
        bankBalanceW=frameLayout.findViewById(R.id.bankBalanceW);


        int slot =getArguments().getInt("slot");
        Player player = MainMenu.myAppDataBase.myDao().getPlayer(slot);
       // double balance= player.getBalance();
        double balanceInBank=player.getBank_deposit();

        bankBalanceW.setText("Balance : "+balanceInBank+"$");
        seekBarWithdraw.setMax((int)balanceInBank);


        seekBarWithdraw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBarMuch.setText(seekBarWithdraw.getProgress()+"$");

            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        withdraw.setOnClickListener(v -> monWithdraw.deliverWithdraw(seekBarWithdraw.getProgress()));

        return  frameLayout;
    }


    public interface OnWithdraw{
         void deliverWithdraw(double withdraw);
    }


    @Override
    public void onAttach(Context context) {

        try{

            monWithdraw=(OnWithdraw)context;

        }catch (ClassCastException e){

            throw  new ClassCastException(context.toString()+" must implement OnWithdraw interface");

        }

        super.onAttach(context);
    }
}

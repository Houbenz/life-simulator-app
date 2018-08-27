package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import beans.Bank;


public class BankFragment extends Fragment {




    private Button depositOpt;
    private Button withdrawOpt;

    private OnDeposit monDeposit;

    public BankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_bank, container, false);



        depositOpt=frameLayout.findViewById(R.id.depositOpt);
        withdrawOpt=frameLayout.findViewById(R.id.withdrawOpt);

        depositOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               monDeposit.depositAndWithdraw("deposit");

            }
        });

        withdrawOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monDeposit.depositAndWithdraw("withdraw");
            }
        });


        return frameLayout;

    }




    public interface OnDeposit{

        public void depositAndWithdraw(String operation);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monDeposit=(OnDeposit)context;

        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnDeposit interface");
        }
    }
}

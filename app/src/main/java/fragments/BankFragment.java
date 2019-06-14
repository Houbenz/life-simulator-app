package fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import com.android.houbenz.lifesimulator.R;

public class BankFragment extends Fragment {




    private Button depositOpt;
    private Button withdrawOpt;
    private SharedPreferences sharedPreferences;
    private OnDeposit monDeposit;
    private View mainBank;

    public BankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_bank, container, false);

        sharedPreferences=getContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);

        depositOpt=frameLayout.findViewById(R.id.depositOpt);
        withdrawOpt=frameLayout.findViewById(R.id.withdrawOpt);

        depositOpt.setOnClickListener(v -> monDeposit.depositAndWithdraw("deposit"));

        withdrawOpt.setOnClickListener(v -> monDeposit.depositAndWithdraw("withdraw"));

        mainBank=frameLayout.findViewById(R.id.mainBank);



        String firstTime = sharedPreferences.getString("firstTimeBank","none");
        if(firstTime.equals("none")){
            showTuto();
            sharedPreferences.edit().putString("firstTimeBank","done").apply();
        }

        return frameLayout;
    }

    public interface OnDeposit{

         void depositAndWithdraw(String operation);
    }

    private void showTuto(){

        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.bankName))
                .setContentText(getString(R.string.bank_section_tuto))
                .setDismissType(DismissType.outside)
                .setTargetView(mainBank)
                .build()
                .show();
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

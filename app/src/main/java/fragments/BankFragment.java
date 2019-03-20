package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import com.example.android.testsharedpreferences.R;

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

        depositOpt.setOnClickListener(v -> monDeposit.depositAndWithdraw("deposit"));

        withdrawOpt.setOnClickListener(v -> monDeposit.depositAndWithdraw("withdraw"));


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

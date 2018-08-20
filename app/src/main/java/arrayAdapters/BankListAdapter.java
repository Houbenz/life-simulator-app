package arrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

import beans.Bank;



public class BankListAdapter extends ArrayAdapter<Bank> {

    public BankListAdapter(@NonNull Context context, ArrayList<Bank> banks) {
        super(context, R.layout.bank_res,banks);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater =LayoutInflater.from(getContext());
        View bankRes = layoutInflater.inflate(R.layout.bank_res,parent,false);


        Bank bank =getItem(position);

        TextView loan =(TextView)bankRes.findViewById(R.id.loanlevel);
        TextView amount=(TextView)bankRes.findViewById(R.id.amount);
        TextView interest=(TextView)bankRes.findViewById(R.id.interest);

        loan.setText("Level : "+bank.getLoan());
        amount.setText("Amount : "+bank.getAmount()+"$");
        interest.setText("Interest : "+bank.getInterest()+"%");

        Log.i("BENZO",bank.getLoan());





        return bankRes;
    }
}

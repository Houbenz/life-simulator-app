package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.BankListAdapter;
import beans.Bank;


public class BankFragment extends Fragment {

    public BankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_bank, container, false);

        ArrayList<Bank> banks=Bank.initBank(getContext());

        BankListAdapter bankListAdapter= new BankListAdapter(getContext(),banks);

        ListView bankView =(ListView)frameLayout.findViewById(R.id.bankView);

        bankView.setAdapter(bankListAdapter);

        bankView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bank bank = (Bank) parent.getItemAtPosition(position);

                Toast.makeText(getContext(),""+bank.getLoan(),Toast.LENGTH_SHORT).show();
            }
        });

        return frameLayout;
    }

}

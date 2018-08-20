package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.BuyGridAdapter;
import beans.Buy;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {


    OnBuyClicked mBuySelected;




    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View fragmentLay= inflater.inflate(R.layout.fragment_buy, container, false);

        GridView gridView=(GridView)fragmentLay.findViewById(R.id.gridBuy);


        ArrayList<Buy> buys =Buy.initBuy(getContext());

        BuyGridAdapter buyGridAdapter =new BuyGridAdapter(getContext(),buys);

        gridView.setAdapter(buyGridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Buy buy= (Buy)parent.getItemAtPosition(position);

                mBuySelected.deliverBuy(buy.getName());

            }
        });

        return fragmentLay;
    }

    public interface OnBuyClicked{

        void deliverBuy(String nameOfFragment);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        try{
            mBuySelected=(OnBuyClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnBuyClicked interface");
        }
    }
}

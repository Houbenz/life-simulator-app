package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;

import java.util.List;

import arrayAdapters.BuyGridAdapter;
import database.MainFragments;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {


    OnMainFragmentClicked mBuySelected;




    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View fragmentLay= inflater.inflate(R.layout.fragment_buy, container, false);

        GridView gridView=(GridView)fragmentLay.findViewById(R.id.gridBuy);


        List<MainFragments> mainFragments =MainMenu.myAppDataBase.myDao().getMainFragments();

        BuyGridAdapter buyGridAdapter =new BuyGridAdapter(getContext(),mainFragments);

        gridView.setAdapter(buyGridAdapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {

            MainFragments mainFrag= (MainFragments) parent.getItemAtPosition(position);

            mBuySelected.deliverMainFragment(mainFrag.getName());

        });

        return fragmentLay;
    }

    public interface OnMainFragmentClicked {

        void deliverMainFragment(String nameOfFragment);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        try{
            mBuySelected=(OnMainFragmentClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnMainFragmentClicked interface");
        }
    }
}

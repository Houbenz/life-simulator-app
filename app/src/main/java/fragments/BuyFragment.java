package fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;



import java.util.List;

import arrayAdapters.BuyGridAdapter;
import database.MainFragments;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {


    OnMainFragmentClicked mBuySelected;


    private SharedPreferences sharedPreferences;
    private GridView gridView;

    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        sharedPreferences=getContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);


        View fragmentLay= inflater.inflate(R.layout.fragment_buy, container, false);

         gridView=(GridView)fragmentLay.findViewById(R.id.gridBuy);


        List<MainFragments> mainFragments =MainMenu.myAppDataBase.myDao().getMainFragments();

        BuyGridAdapter buyGridAdapter =new BuyGridAdapter(getContext(),mainFragments);

        gridView.setAdapter(buyGridAdapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {

            MainFragments mainFrag= (MainFragments) parent.getItemAtPosition(position);

            mBuySelected.deliverMainFragment(mainFrag.getName());

        });



        String firstTime=sharedPreferences.getString("firstTimeBuy","none");

        if(firstTime.equals("none")) {
            showTuto();
            sharedPreferences.edit().putString("firstTimeBuy","finished").apply();
        }

        return fragmentLay;
    }


    private void showTuto(){

        new GuideView.Builder(getContext())
                .setTitle("Buying")
                .setContentText("From this section you can buy things, Food, medicines," +
                        " and other stuff like cars !, beware don't spend too much ")
                .setDismissType(DismissType.outside)
                .setTargetView(gridView)
                .build()
                .show();
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

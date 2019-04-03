package fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.List;

import arrayAdapters.GiftListAdapter;
import database.Gift;
import viewmodels.ViewModelCars;
import viewmodels.ViewModelGift;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiftFragment extends Fragment {


    public GiftFragment() {
        // Required empty public constructor
    }

    private ViewModelGift viewModelGift;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_gift, container, false);
        ListView giftList = fragment.findViewById(R.id.giftList);
        List<Gift> gifts = MainMenu.myAppDataBase.myDao().getGifts();

        GiftListAdapter giftListAdapter = new GiftListAdapter(getContext(),gifts);

        giftList.setAdapter(giftListAdapter);

        viewModelGift=ViewModelProviders.of(getActivity()).get(ViewModelGift.class);


        giftList.setOnItemClickListener((parent, view, position, id) -> {
            Gift gift = (Gift)parent.getItemAtPosition(position);

            viewModelGift.setGift(gift);
        });




        return  fragment;
    }

}

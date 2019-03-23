package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.houbenz.lifesimulator.MainMenu;
import com.android.houbenz.lifesimulator.R;

import java.util.List;

import arrayAdapters.StoreListAdapter;
import database.Store;
import database.Acquired_Stores;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    OnStoreClicked monStoreClicked;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment= inflater.inflate(R.layout.fragment_store, container, false);


        int slot =getArguments().getInt("slot");
        List<Store> stores =MainMenu.myAppDataBase.myDao().getStores();

        ListView storeView =fragment.findViewById(R.id.storeView);


        StoreListAdapter storeListAdapter =new StoreListAdapter(getContext(),stores,slot);

        storeView.setAdapter(storeListAdapter);

        storeView.setOnItemClickListener((parent, view, position, id) -> {

            Store store = (Store)parent.getItemAtPosition(position);
            monStoreClicked.deliverStore(store);
        });


        return fragment;
    }

    public interface  OnStoreClicked {
        public void deliverStore(Store store);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monStoreClicked=(OnStoreClicked)context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement OnStoreClicked interface");
        }
    }
}

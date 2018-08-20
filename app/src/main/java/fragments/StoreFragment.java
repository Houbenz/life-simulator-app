package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

import arrayAdapters.StoreListAdapter;
import beans.Store;

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

        ArrayList<Store> stores =Store.initStore(getContext());

        ListView storeView =fragment.findViewById(R.id.storeView);

        StoreListAdapter storeListAdapter =new StoreListAdapter(getContext(),stores);

        storeView.setAdapter(storeListAdapter);

        storeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Store store = (Store)parent.getItemAtPosition(position);
                monStoreClicked.deliverStore(store);
            }
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

package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.testsharedpreferences.MainMenu;
import com.example.android.testsharedpreferences.R;
import java.util.List;

import arrayAdapters.PharmacyListAdapter;
import database.Medicine;

/**
 * A simple {@link Fragment} subclass.
 */
public class PharmacyFragment extends Fragment {

    OnMedicineClicked monMedicineClicked;

    public PharmacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.fragment_pharmacy, container, false);

        List<Medicine> medicines =MainMenu.myAppDataBase.myDao().getMedicines();

        ListView pharmacyView =fragment.findViewById(R.id.pharmacyView);

        PharmacyListAdapter pharmacyListAdapter=new PharmacyListAdapter(getContext(),medicines);

        pharmacyView.setAdapter(pharmacyListAdapter);

        pharmacyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Medicine medicine =(Medicine)parent.getItemAtPosition(position);

                monMedicineClicked.deliverMedicine(medicine);


            }
        });


        return  fragment;

    }

    public interface OnMedicineClicked{
         void deliverMedicine(Medicine medicine);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            monMedicineClicked=(OnMedicineClicked)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnMedicineClicked interface");
        }
    }
}

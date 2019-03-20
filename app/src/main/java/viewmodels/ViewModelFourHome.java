package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import database.Acquired_Furnitures;
import database.Furniture;

public class ViewModelFourHome extends ViewModel {

    private MutableLiveData<List<Acquired_Furnitures>> acquired_furn = new MutableLiveData<List<Acquired_Furnitures>>();


    public void setAcquired_furn(List<Acquired_Furnitures> item){acquired_furn.setValue(item);}

    public LiveData<List<Acquired_Furnitures>> getAcquired_furn(){ return acquired_furn;}

}

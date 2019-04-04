package viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import database.Acquired_Furnitures;

public class ViewModelFourHome extends ViewModel {

    private MutableLiveData<List<Acquired_Furnitures>> acquired_furn = new MutableLiveData<List<Acquired_Furnitures>>();



    public LiveData<List<Acquired_Furnitures>> getAcquired_furn(){ return acquired_furn;}

    public void setAcquired_furn(List<Acquired_Furnitures> item){acquired_furn.setValue(item);}
}

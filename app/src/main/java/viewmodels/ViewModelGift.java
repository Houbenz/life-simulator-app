package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import database.Gift;

public class ViewModelGift extends ViewModel {

    private MutableLiveData<Gift> gift = new MutableLiveData<Gift>();

    public void setGift(Gift gift){this.gift.setValue(gift);}

    public LiveData<Gift> getGift(){return gift;}
}

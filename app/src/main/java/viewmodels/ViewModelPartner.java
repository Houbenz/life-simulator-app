package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ViewModelPartner extends ViewModel {

    private MutableLiveData<Boolean> looking = new MutableLiveData<Boolean>();


    public void setIsLooking(Boolean isLooking){
        this.looking.setValue(isLooking);
    }

    public LiveData<Boolean> isLooking(){
        return looking;
    }


}

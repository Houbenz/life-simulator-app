package viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelPartner extends ViewModel {

    private MutableLiveData<Boolean> looking = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> foundPartner = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> breakUp = new MutableLiveData<Boolean>();



    public void setIsLooking(Boolean isLooking){
        this.looking.setValue(isLooking);
    }

    public LiveData<Boolean> isLooking(){
        return looking;
    }

    public  void setFoundPartner(Boolean foundPartner){this.foundPartner.setValue(foundPartner);}

    public LiveData<Boolean> isFoundPartner(){return foundPartner;}

    public  void setBreakUp(Boolean breakUp){this.breakUp.setValue(breakUp);}

    public LiveData<Boolean> isBreakUp(){return breakUp;}


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

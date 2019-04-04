package viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelPartner extends ViewModel {

    private MutableLiveData<Integer> relationBar = new MutableLiveData<Integer>();
    private MutableLiveData<Boolean> foundPartner = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> breakUp = new MutableLiveData<Boolean>();

    public void setRelationBar(Integer relationBar){this.relationBar.setValue(relationBar);}

    public LiveData<Integer> getRelationBar(){
        return relationBar;
    }


    public  void setBreakUp(Boolean breakUp){this.breakUp.setValue(breakUp);}

    public LiveData<Boolean> isBreakUp(){return breakUp;}

    public  void setFoundPartner(Boolean foundPartner){this.foundPartner.setValue(foundPartner);}

    public LiveData<Boolean> isFoundPartner(){return foundPartner;}
}

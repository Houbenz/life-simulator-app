package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import beans.Furniture;

public class ViewModelFourHome extends ViewModel {

    private MutableLiveData<ArrayList<Furniture>> furniture = new MutableLiveData<ArrayList<Furniture>>();


    public ViewModelFourHome(){

        furniture.setValue( new ArrayList<Furniture>());
    }



    public void setFurniture(ArrayList<Furniture> item){
        furniture.setValue(item);
    }

    public LiveData<ArrayList<Furniture>> getFurniture(){
        return furniture;
    }
}

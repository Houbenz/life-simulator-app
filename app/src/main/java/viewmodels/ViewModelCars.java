package viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import database.Car;

public class ViewModelCars extends ViewModel {

    private MutableLiveData<Car> car = new MutableLiveData<>();

    public LiveData<Car> getCar() {
        return car;
    }

    public void setCar(Car acquired_cars) {

        this.car.setValue(acquired_cars);

    }
}

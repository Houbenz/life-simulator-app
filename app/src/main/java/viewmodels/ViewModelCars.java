package viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

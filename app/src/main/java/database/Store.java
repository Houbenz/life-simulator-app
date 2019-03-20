package database;


import android.arch.persistence.room.Entity;

@Entity
public class Store extends Purchases {


    private double income ;


    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}

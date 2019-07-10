package database;


import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

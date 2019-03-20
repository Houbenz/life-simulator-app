package database;


import android.arch.persistence.room.Entity;

@Entity
public class Food extends Purchases {


    private int benefit;
    private String description;

    public int getBenefit() {
        return benefit;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


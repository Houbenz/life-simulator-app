package database;


import androidx.room.Entity;

@Entity
public class Medicine extends  Purchases {


    private int benefit;


    public int getBenefit() {
        return benefit;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }
}


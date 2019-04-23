package database;

import androidx.room.Entity;
@Entity
public class House extends Purchases {

    private int bonusHealth;
    private int bonusEnergy;


    public int getBonusHealth() {
        return bonusHealth;
    }

    public void setBonusHealth(int bonusHealth) {
        this.bonusHealth = bonusHealth;
    }

    public int getBonusEnergy() {
        return bonusEnergy;
    }

    public void setBonusEnergy(int bonusEnergy) {
        this.bonusEnergy = bonusEnergy;
    }
}

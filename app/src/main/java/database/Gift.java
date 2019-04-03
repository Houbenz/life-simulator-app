package database;

import android.arch.persistence.room.Entity;

@Entity
public class Gift extends Purchases{

    private int giftCount;

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }
}

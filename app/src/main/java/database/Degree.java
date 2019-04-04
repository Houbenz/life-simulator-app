package database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Degree extends Purchases {


    private int progress ;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

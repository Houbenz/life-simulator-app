package database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name ;


}

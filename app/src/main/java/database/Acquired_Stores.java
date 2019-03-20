package database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity =Player.class,childColumns = "player_id",parentColumns = "id",onDelete = CASCADE,onUpdate = CASCADE),
        @ForeignKey(entity =Store.class,childColumns = "store_id",parentColumns = "id",onDelete = CASCADE,onUpdate = CASCADE)
})
public class Acquired_Stores {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player_id;
    private int store_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}

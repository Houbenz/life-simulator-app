package database;


import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity =Player.class,childColumns = "player_id",parentColumns = "id",onDelete = CASCADE,onUpdate = CASCADE),
        @ForeignKey(entity =Store.class,childColumns = "store_id",parentColumns = "id",onDelete = CASCADE,onUpdate = CASCADE)
})
public class Acquired_Stores implements Serializable {


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

package database;


import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Player.class,childColumns = "player_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE),
        @ForeignKey(entity = House.class,childColumns = "house_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE)})

public class Acquired_Houses implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private int player_id;
    private int house_id;
    private String imgUrl;

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

    public int getHouse_id() {
        return house_id;
    }

    public void setHouse_id(int house_id) {
        this.house_id = house_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

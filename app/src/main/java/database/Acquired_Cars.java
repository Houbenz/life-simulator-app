package database;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Player.class,childColumns = "player_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE),
        @ForeignKey(entity = Car.class,childColumns = "car_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE)})


public class Acquired_Cars implements  Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player_id;

    private int car_id;

    private String imgUrl;

    private String available;


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

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}

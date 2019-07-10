package database;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Player.class,childColumns = "player_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE),
        @ForeignKey(entity = Furniture.class,childColumns = "furn_id",parentColumns = "id",onUpdate = CASCADE,onDelete = CASCADE)
})
public class Acquired_Furnitures implements Serializable{


    @PrimaryKey(autoGenerate = true)
    private int id ;

    private int player_id;
    private int furn_id;

    private String imgurl;

    private String furnitureType;

    private  String available;

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

    public int getFurn_id() {
        return furn_id;
    }

    public void setFurn_id(int furn_id) {
        this.furn_id = furn_id;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }
}

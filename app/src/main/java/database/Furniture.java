package database;

import androidx.room.Entity;

@Entity
public class Furniture extends Purchases {


    private String furnitureType;


    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }
}

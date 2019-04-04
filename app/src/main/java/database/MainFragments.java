package database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MainFragments  {


    @PrimaryKey(autoGenerate = true)
    private  int id;

    private String name;

    private String color;

    private String image_Uri;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage_Uri() {
        return image_Uri;
    }

    public void setImage_Uri(String image_Uri) {
        this.image_Uri = image_Uri;
    }
}

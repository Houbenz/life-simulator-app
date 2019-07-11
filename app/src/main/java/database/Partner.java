package database;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Partner  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name ;
    private String image;
    private int likeness;

    private String dating ;

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikeness() {
        return likeness;
    }

    public void setLikeness(int likeness) {
        this.likeness = likeness;
    }




}

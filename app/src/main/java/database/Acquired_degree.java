package database;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Player.class,parentColumns = "id",childColumns = "player_id",onUpdate = CASCADE,onDelete = CASCADE),
        @ForeignKey(entity = Degree.class,parentColumns = "id",childColumns = "degree_id",onUpdate = CASCADE,onDelete = CASCADE)
})
public class Acquired_degree implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player_id;

    private int degree_id;

    private String degree_Name;

    private int player_progress;

    private String available;


    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
    public int getPlayer_progress() {
        return player_progress;
    }

    public void setPlayer_progress(int player_progress) {
        this.player_progress = player_progress;
    }

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

    public int getDegree_id() {
        return degree_id;
    }

    public void setDegree_id(int degree_id) {
        this.degree_id = degree_id;
    }

    public String getDegree_Name() {
        return degree_Name;
    }

    public void setDegree_Name(String degree_Name) {
        this.degree_Name = degree_Name;
    }
}

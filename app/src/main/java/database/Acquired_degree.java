package database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Player.class,parentColumns = "id",childColumns = "player_id",onUpdate = CASCADE,onDelete = CASCADE),
        @ForeignKey(entity = Degree.class,parentColumns = "id",childColumns = "degree_id",onUpdate = CASCADE,onDelete = CASCADE)
})
public class Acquired_degree {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player_id;

    private int degree_id;

    private String degree_Name;


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

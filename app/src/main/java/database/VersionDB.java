package database;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VersionDB{


    private String version;

    @PrimaryKey(autoGenerate = true)
    private int id ;


    public VersionDB(String version,int id){
        this.version=version;
        this.id=id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

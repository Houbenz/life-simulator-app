package database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VersionDB {


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

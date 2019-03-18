package database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDao {


    @Insert()
     void addPlayer(Player player);


    @Query("select * from Player")
     List<Player> getPlayers();

}

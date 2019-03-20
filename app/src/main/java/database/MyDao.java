package database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MyDao {


    @Insert
     void addPlayer(Player player);

    @Insert
    void addDegree(Degree degree);

    @Insert
    void addWork(Work work);

    @Insert
    void  addAcquired_Furniture(Acquired_Furnitures acquired_furnitures);
    @Insert
    void addAcquired_degree(Acquired_degree acquired_degree);

    @Insert
    void addAcquired_Store(Acquired_Stores acquired_stores);

    @Insert
    void addFood(Food food);

    @Insert
    void addMedicine(Medicine medicine);

    @Insert
    void addMainFragment(MainFragments mainFragments);

    @Insert
    void addStore(Store store);

    @Insert
    void addFurnitures(Furniture furniture);


    @Query("select * from acquired_furnitures where player_id = :id")
    List<Acquired_Furnitures> getAcquiredFurnitures(int id);

    @Query("select * from Acquired_Stores")
    List<Acquired_Stores> getAcquiredStores();

    @Query("select * from Acquired_degree")
    List<Acquired_degree> getAcquiredDegrees();

    @Query("select * from Player")
     List<Player> getPlayers();

    @Query("select * from Degree where name =:id")
    Degree getDegree(int id);

    @Query("select * from Degree")
    List<Degree> getDegrees();

    @Query("select * from Work")
    List<Work> getWorks();

    @Query("select * from MainFragments")
    List<MainFragments> getMainFragments();

    @Query("select * from Food")
    List<Food> getFoods();

    @Query("select * from Furniture")
    List<Furniture> getFurnitures();

    @Query("select * from Medicine")
    List<Medicine> getMedicines();

    @Query("select * from Store")
    List<Store> getStores();

    @Query("select * from Player where id = :id")
    Player getPlayer(int id);

    @Query("select * from Acquired_Furnitures where furn_id=:id and player_id=:playerid")
    Acquired_Furnitures getAcqFurn(int id, int playerid);

    @Update
    void updateAcquired_Furnitures(Acquired_Furnitures acquired_furnitures);

    @Update
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);


}

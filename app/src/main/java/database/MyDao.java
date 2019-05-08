package database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    void addPartner(Partner partner);

    @Insert
    void addAcquired_Car(Acquired_Cars acquired_cars);

    @Insert
    void  addAcquired_Furniture(Acquired_Furnitures acquired_furnitures);
    @Insert
    void addAcquired_degree(Acquired_degree acquired_degree);

    @Insert
    void addAcquired_Store(Acquired_Stores acquired_stores);

    @Insert
    void addAcquired_House(Acquired_Houses acquiredHouses);

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

    @Insert
    void initDBVersion(VersionDB version);

    @Insert
    void addCar(Car car);

    @Insert
    void addGift(Gift gift);

    @Insert
    void addHouse(House house);


    @Query("select * from House where id =:id")
    House getHouse(int id);

    @Query("select * from House")
    List<House> gethouses();

    @Query("select * from Partner")
    List<Partner> getPartners();

    @Query("select * from Gift")
    List<Gift> getGifts();

    @Query("select * from Car")
    List<Car> getCars();

    @Query("select * from Acquired_Houses where player_id=:id")
    List<Acquired_Houses> getAcquiredHouses(int id);


    @Query("select * from Acquired_Houses where player_id=:id and house_id=:houseId")
   Acquired_Houses getAcqHouse(int id,int houseId);

    @Query("select * from Acquired_Cars where player_id=:id")
    List<Acquired_Cars> getAcquiredCars(int id);

    @Query("select * from acquired_furnitures where player_id = :id")
    List<Acquired_Furnitures> getAcquiredFurnitures(int id);

    @Query("select * from Acquired_Stores where player_id=:id")
    List<Acquired_Stores> getAcquiredStores(int id);

    @Query("select * from Acquired_degree where player_id=:id")
    List<Acquired_degree> getAcquiredDegrees(int id);

    @Query("select * from Acquired_degree where degree_id=:degreeid and player_id=:playerid")
    Acquired_degree getAcqDegr(int playerid,int degreeid);

    @Query("select * from Degree where name =:id")
    Degree getDegree(int id);

    @Query("select * from Degree")
    List<Degree> getDegrees();

    @Query("select * from Work")
    List<Work> getWorks();

    @Query("select income from Work where name like :name")
    double work_incorme(String name);

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

    @Query("select * from VersionDB")
    VersionDB getVersionDB();

    @Query("select * from Acquired_Furnitures where furn_id=:id and player_id=:playerid")
    Acquired_Furnitures getAcqFurn(int id, int playerid);

    @Query("select income from Store where id =:id")
    double getStoreIncome(int id);

    @Query("select * from Acquired_Cars where player_id =:id and car_id=:idc")
    Acquired_Cars getAcquiredCars(int id,int idc);

    @Query("select * from Partner where dating like 'true' ")
    Partner getDatingPartner();

    @Update
    void updateAcquired_car(Acquired_Cars cars);

    @Update
    void updateHouse(House house);
    @Update
    void updatePartner(Partner partner);

    @Update
    void updateCar(Car car);
    @Update
    void update_Acquired_Degree(Acquired_degree acquired_degree);

    @Update
    void updateAcquired_Furnitures(Acquired_Furnitures acquired_furnitures);

    @Update
    void updateWork(Work work);

    @Update
    void updateFurniture(Furniture furniture);

    @Update
    void updateFood(Food food);

    @Update
    void updateStore(Store store);

    @Update
    void updateMedicine(Medicine medicine);

    @Update
    void updateDegree(Degree degree);

    @Update
    int updatePlayer(Player player);

    @Update
    void updateGift(Gift gift);

    @Update
    void updateMainFragment(MainFragments mainFragments);

    @Update
    void updateVerionDB(VersionDB version);

    @Delete
    void deletePlayer(Player player);

    @Delete
    void deleteGift(Gift gift);


    //Get Gifts individually

    @Query("select * from Gift where name like 'Roses'")
    Gift getRoses();

    @Query("select * from Gift where name like 'Chocolate'")
    Gift getChocolate();

    @Query("select * from Gift where name like 'Jewelry'")
    Gift getJewelry();

    //Counts
    @Query("select count(*) from Work")
    int workNumber();

    @Query("select count(*) from store")
    int storeNumber();

    @Query("select count(*) from Furniture")
    int furnitureNumber();

    @Query("select count(*) from Food")
    int foodNumber();

    @Query("select count(*) from Degree")
    int degreeNumber();

    @Query("select count(*) from Medicine")
    int medicineNumber();

    @Query("select count(*) from Car")
    int carsNumber();

    @Query("select count(*) from MainFragments")
    int fragmentNumber();

    @Query("select count(*) from Gift")
    int giftNumber();

    @Query("select count(*) from Partner")
    int partnerNumber();

    @Query("select count(*) from House")
    int houseNumber();
}

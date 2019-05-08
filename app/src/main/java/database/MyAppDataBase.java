package database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities ={Player.class,Degree.class,Work.class,Purchases.class,Gift.class,Acquired_degree.class,Acquired_Furnitures.class,
        Acquired_Stores.class,Acquired_Cars.class,Food.class,Furniture.class,MainFragments.class,
        Medicine.class,Store.class,VersionDB.class,Car.class,Partner.class,House.class,Acquired_Houses.class
},version = 25)
public abstract class MyAppDataBase extends RoomDatabase {


    public abstract MyDao myDao();
}

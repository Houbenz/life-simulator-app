package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities ={Player.class,Degree.class,Work.class,Purchases.class,Gift.class,Acquired_degree.class,Acquired_Furnitures.class,
        Acquired_Stores.class,Acquired_Cars.class,Food.class,Furniture.class,MainFragments.class,Medicine.class,Store.class,VersionDB.class,Car.class
},version = 21)
public abstract class MyAppDataBase extends RoomDatabase {


    public abstract MyDao myDao();
}

package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities ={Player.class,Degree.class,Work.class,Purchases.class,Acquired_degree.class,Acquired_Furnitures.class,
        Acquired_Stores.class, Food.class,Furniture.class,MainFragments.class,Medicine.class,Store.class,VersionDB.class},version = 15)
public abstract class MyAppDataBase extends RoomDatabase {


    public abstract MyDao myDao();
}

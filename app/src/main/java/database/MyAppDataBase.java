package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities ={Player.class},version = 1)
public abstract class MyAppDataBase extends RoomDatabase {


    public abstract MyDao myDao();
}

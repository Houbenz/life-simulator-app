package com.houbenz.lifesimulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import database.MyAppDataBase;
import com.houbenz.lifesimulator.MainMenu;

import static com.houbenz.lifesimulator.MainMenu.MIGRATION_14_15;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_15_16;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_16_17;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_17_18;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_18_19;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_19_20;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_20_21;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_21_22;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_22_23;
import static com.houbenz.lifesimulator.MainMenu.MIGRATION_23_24;

@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DB="life_simulatordb";

    @Rule
    public MigrationTestHelper helper;


    public MigrationTest(){
        helper=new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                MyAppDataBase.class.getCanonicalName(),new FrameworkSQLiteOpenHelperFactory());
    }


    @Test
    public  void migrate23To24() throws IOException{
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB,24);

      /*  db.execSQL("insert into player (id,name,work,income,work_income,creation_date,work_image_path,work_time,store_income," +
                "balance,dating,bank_deposit,day,hour,minute,level,level_progress,max_progress,healthbar,energybar,hungerbar,relationBar)" +
                "values(4,'houhou','worker',0,0,'223355','path',8,0,10,'false',0,0,0,0,0,0,0,10,10,10,10)");

        db.close();*/

        db=helper.runMigrationsAndValidate(TEST_DB,24,true,
                MIGRATION_14_15,MIGRATION_15_16,MIGRATION_16_17,MIGRATION_17_18,MIGRATION_18_19,MIGRATION_19_20,MIGRATION_20_21,MIGRATION_21_22
                ,MIGRATION_22_23,MIGRATION_23_24);
    }
}
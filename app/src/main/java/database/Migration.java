package database;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration {


    public  static final androidx.room.migration.Migration MIGRATION_14_15 = new androidx.room.migration.Migration(14, 15) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Acquired_Degree add column player_progress integer default 0 not null");
            database.execSQL("alter table Acquired_Degree add column available varchar");

            database.execSQL("alter table degree add column progress integer default 0 not null");

        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_15_16 = new androidx.room.migration.Migration(15,16) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Car(" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "price real," +
                    "imgUrl text)");
        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_16_17 = new androidx.room.migration.Migration(16,17) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Acquired_Cars(id INTEGER primary key autoincrement not null," +
                    "car_id INTEGER not null," +
                    "player_id INTEGER not null," +
                    "foreign key(player_id) references Player(id) on delete cascade on update cascade," +
                    "foreign key(car_id) references Car(id) on delete cascade on update cascade )");
        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_17_18 = new androidx.room.migration.Migration(17,18) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE Gift(" +
                    "id integer PRIMARY KEY AUTOINCREMENT not null," +
                    "name TEXT," +
                    "price REAL not null," +
                    "imgUrl TEXT)");
        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_18_19 = new androidx.room.migration.Migration(18,19) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Acquired_Gifts(id INTEGER primary key autoincrement not null," +
                    "gift_id INTEGER not null," +
                    "player_id INTEGER not null," +
                    "foreign key(player_id) references Player(id) on delete cascade on update cascade," +
                    "foreign key(gift_id) references Gift(id) on delete cascade on update cascade )");
        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_19_20 = new androidx.room.migration.Migration(19,20) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("drop table Acquired_Gifts");
            database.execSQL("alter table Gift add column giftCount INTEGER not null default 0");
            database.execSQL("alter table Player add column dating TEXT");

        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_20_21 = new androidx.room.migration.Migration(20,21) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Player add column dating TEXT");
        }
    };

    public  static  final androidx.room.migration.Migration MIGRATION_21_22 = new androidx.room.migration.Migration(21,22) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table Player add column relationBar INTEGER not null default 0");
        }
    };

    public    static  final androidx.room.migration.Migration MIGRATION_22_23 = new androidx.room.migration.Migration(22,23) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table Partner(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "name TEXT," +
                    "image TEXT," +
                    "dating TEXT," +
                    "INTEGER likeness not null default 0)");
        }
    };
    public  static  final androidx.room.migration.Migration MIGRATION_23_24 = new androidx.room.migration.Migration(23,24) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("create table House(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "name TEXT," +
                    "imgUrl TEXT," +
                    "INTEGER bonusH not null default 0," +
                    "INTEGER bonusE not null default 0," +
                    "real price not null default 0)");

            database.execSQL("create table Acquired_Houses(id INTEGER primary key autoincrement not null," +
                    "house_id INTEGER not null," +
                    "player_id INTEGER not null," +
                    "foreign key(player_id) references Player(id) on delete cascade on update cascade," +
                    "foreign key(gift_id) references Gift(id) on delete cascade on update cascade )");
        }
    };
}

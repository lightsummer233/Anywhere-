package com.absinthe.anywhere_.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.absinthe.anywhere_.model.AnywhereEntity;
import com.absinthe.anywhere_.model.PageEntity;

@Database(entities = {AnywhereEntity.class, PageEntity.class}, version = 4, exportSchema = false)
public abstract class AnywhereRoomDatabase extends RoomDatabase {

    public abstract AnywhereDao anywhereDao();

    private static AnywhereRoomDatabase INSTANCE;

    public static AnywhereRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnywhereRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AnywhereRoomDatabase.class, "anywhere_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE anywhere_new (id TEXT NOT NULL, app_name TEXT NOT NULL, param_1 TEXT NOT NULL, param_2 TEXT, param_3 TEXT, description TEXT, type INTEGER NOT NULL DEFAULT 0, time_stamp TEXT NOT NULL, PRIMARY KEY(id))");
            // Copy the data
            database.execSQL(
                    "INSERT INTO anywhere_new (id, app_name, param_1, param_2, param_3, description, type, time_stamp) SELECT time_stamp, app_name, param_1, param_2, param_3, description, type, time_stamp FROM anywhere_table");
            // Remove the old table
            database.execSQL("DROP TABLE anywhere_table");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE anywhere_new RENAME TO anywhere_table");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("UPDATE anywhere_table SET param_3=null");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE anywhere_table "
                    + " ADD COLUMN category TEXT");

            //Create Page table
            database.execSQL(
                    "CREATE TABLE page_table (title TEXT NOT NULL, priority INTEGER NOT NULL, time_stamp TEXT NOT NULL, PRIMARY KEY(title))");
        }
    };
}
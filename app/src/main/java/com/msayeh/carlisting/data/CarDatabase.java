package com.msayeh.carlisting.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Car.class}, version = 1)
abstract public class CarDatabase extends RoomDatabase {

    private static CarDatabase instance;

    public static synchronized CarDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CarDatabase.class,
                    "car_database")
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract CarDao carDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CarDao dao = instance.carDao();

                    dao.insert(new Car("Mitsubishi", "lancer", "2016"));
                    dao.insert(new Car("Mitsubishi", "lancer", "2016"));
                    dao.insert(new Car("Mitsubishi", "lancer", "2016"));
                    dao.insert(new Car("Mitsubishi", "lancer", "2016"));

                }
            }).start();
        }
    };
}

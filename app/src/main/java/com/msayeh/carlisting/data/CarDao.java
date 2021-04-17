package com.msayeh.carlisting.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CarDao {

    @Update
    void update(Car car);

    @Insert
    void insert(Car car);

    @Delete
    void delete(Car car);

    @Query("SELECT * FROM CARS_TABLE")
    List<Car> getAllCars();
}

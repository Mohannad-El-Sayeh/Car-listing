package com.msayeh.carlisting.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cars_table")
public class Car {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String model;
    private String year;

    public Car(String name, String model, String year) {
        this.name = name;
        this.model = model;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }
}

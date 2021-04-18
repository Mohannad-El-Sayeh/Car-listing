package com.msayeh.carlisting.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cars_table")
public class Car implements Serializable {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

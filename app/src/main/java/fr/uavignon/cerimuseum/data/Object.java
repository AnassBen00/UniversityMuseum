package fr.uavignon.cerimuseum.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "museum_table", indices = {@Index(value = {"name", "_id"},
        unique = true)})

public class Object {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="id_num")
    private long id_num;

    @NonNull
    @ColumnInfo(name="_id")
    private String id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="category")
    private String category;

    @ColumnInfo(name="descrption")
    private String descrption;

    @ColumnInfo(name="timeFrame")
    private String timeFrame;

    @ColumnInfo(name="year")
    private Integer year;

    @ColumnInfo(name="brand")
    private String brand;

    @ColumnInfo(name="technicalDetails")
    private String technicalDetails;

    @ColumnInfo(name="working")
    private String working;

   @ColumnInfo(name="pictures")
    private String pictures;


    @Ignore
    public Object( @NonNull String id) {
        this.id = id;
    }

    public Object(@NonNull String name, @NonNull String category, String descrption, String timeFrame, Integer year, String brand, String technicalDetails, String working, String pictures) {
        this.name = name;
        this.category = category;
        this.descrption = descrption;
        this.timeFrame = timeFrame;
        this.year = year;
        this.brand = brand;
        this.technicalDetails = technicalDetails;
        this.working = working;
        this.pictures = pictures;
    }

    @NonNull
    public long getId_num() {
        return id_num;
    }

    public void setId_num(@NonNull long id_num) {
        this.id_num = id_num;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTechnicalDetails() {
        return technicalDetails;
    }

    public void setTechnicalDetails(String technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "Object{" +
                 + id_num +
                  id + '\'' +
                  name + '\'' +
                  category + '\'' +
                  descrption + '\'' +
                  timeFrame + '\'' +
                   year +
                  brand + '\'' +
                 technicalDetails + '\'' +
                '}';
    }
}

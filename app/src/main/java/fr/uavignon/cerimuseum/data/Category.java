package fr.uavignon.cerimuseum.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Category_table")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="_id")
    private long id;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    public long getId() {
        return id;
    }

    public Category(@NonNull String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public void setName(String name) {
        this.name = name;
    }

}

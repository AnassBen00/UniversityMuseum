package fr.uavignon.cerimuseum.data.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.uavignon.cerimuseum.data.Object;

@Dao
public interface MuseumDAO {
    @Insert
    long insert(Object object);

    @Query("DELETE FROM museum_table")
    void deleteAll();

    @Query("SELECT * from museum_table ORDER BY name ASC")
    LiveData<List<fr.uavignon.cerimuseum.data.Object>> getAllObjects();

    @Query("SELECT * FROM museum_table WHERE id_num = :id")
    Object getObjectById(long id);

    @Query("SELECT * from museum_table ORDER BY name DESC")
    LiveData<List<fr.uavignon.cerimuseum.data.Object>> getAllObjectsDesc();

    @Query("SELECT * from museum_table ORDER BY year DESC")
    LiveData<List<fr.uavignon.cerimuseum.data.Object>> getAllObjectsNewest();

    @Query("SELECT * from museum_table ORDER BY year ASC")
    LiveData<List<fr.uavignon.cerimuseum.data.Object>> getAllObjectsOldest();

    @Update
    int update(Object object);


}

package fr.uavignon.cerimuseum.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.uavignon.cerimuseum.ListViewModel;
import fr.uavignon.cerimuseum.data.Category;
import fr.uavignon.cerimuseum.data.MuseumRepository;
import fr.uavignon.cerimuseum.data.Object;
import fr.uavignon.cerimuseum.data.ObjectResponse;
import fr.uavignon.cerimuseum.data.getCollection;
import fr.uavignon.cerimuseum.data.webservice.OWMInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Database(entities = {Object.class, Category.class}, version = 1, exportSchema = false)

abstract
public class MuseumRoomDatabase extends RoomDatabase {

    private static final String TAG = MuseumRoomDatabase.class.getSimpleName();

    public abstract MuseumDAO museumDAO();

    private static MuseumRoomDatabase INSTANCE;

    public static List<Object> objects;

    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MuseumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MuseumRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    // without populate
                        /*
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    WeatherRoomDatabase.class,"book_database")
                                    .build();
                     */

                    // with populate
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    MuseumRoomDatabase.class,"museum_database")
                                    .addCallback(sRoomDatabaseCallback)
                                    .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    databaseWriteExecutor.execute(() -> {
                        // Populate the database in the background.
                        MuseumDAO dao = INSTANCE.museumDAO();
                        dao.deleteAll();
                    });

                }
            };

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}

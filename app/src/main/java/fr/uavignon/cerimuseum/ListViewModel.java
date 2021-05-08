package fr.uavignon.cerimuseum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.uavignon.cerimuseum.data.MuseumRepository;
import fr.uavignon.cerimuseum.data.Object;

public class ListViewModel extends AndroidViewModel {
    public MuseumRepository repository;
    private LiveData<List<Object>> allObjects;
    private LiveData<List<Object>> objectsDesc;
    private LiveData<List<Object>> objectsOldest;
    private LiveData<List<Object>> objectsNewest;


    public ListViewModel(@NonNull Application application) {
        super(application);
        repository = repository.get(application);
        allObjects = repository.getAllObjects();
        objectsDesc = repository.getObjects();
        objectsOldest = repository.getobjectsOldest();
        objectsNewest = repository.getObjectsNewest();
    }

    LiveData<List<Object>> getAllObjects() {
        return allObjects;
    }

    public LiveData<List<Object>> getObjects(){
        return objectsDesc;
    }

    public LiveData<List<Object>> getOldestObjects(){
        return objectsOldest;
    }

    public LiveData<List<Object>> getNewestObjects(){
        return objectsNewest;
    }

}

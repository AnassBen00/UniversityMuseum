package fr.uavignon.cerimuseum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.uavignon.cerimuseum.data.Category;
import fr.uavignon.cerimuseum.data.MuseumRepository;
import fr.uavignon.cerimuseum.data.Object;

public class CategoriesViewModel extends AndroidViewModel {

    private MuseumRepository repository;
    private Map<String, List<Object>> categories;
    private LiveData<List<Object>> musuemObjects;
    private LiveData<List<Category>> cats;

    public CategoriesViewModel(@NonNull Application application) throws IOException {
        super(application);
        repository = MuseumRepository.get(application);
        cats = repository.getAllCategories();
        musuemObjects = repository.getObjects();
    }

    public LiveData<List<Category>> getAllCategories(){
        return cats;
    }
}

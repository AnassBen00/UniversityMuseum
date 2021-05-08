package fr.uavignon.cerimuseum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fr.uavignon.cerimuseum.data.MuseumRepository;
import fr.uavignon.cerimuseum.data.Object;

public class DetailViewModel extends AndroidViewModel {

    private MuseumRepository repository;
    private MutableLiveData<Object> objet;
    private MutableLiveData<Throwable> webServiceThrowable;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        repository = MuseumRepository.get(application);
        objet = new MutableLiveData<>();
        webServiceThrowable = new MutableLiveData<>();
    }

    public void setObjet(long id) {
        repository.getObject(id);
        objet = repository.getSelectedObject();
    }
    LiveData<Object> getObjet() {
        return objet;
    }

    public void loadCity(Object o){
        repository.loadMuseumObject(o);
    }

    MutableLiveData<Throwable> getWebServiceThrowable(){
        return webServiceThrowable;
    }
}

package fr.uavignon.cerimuseum.data;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import fr.uavignon.cerimuseum.data.database.MuseumDAO;
import fr.uavignon.cerimuseum.data.database.MuseumRoomDatabase;
import fr.uavignon.cerimuseum.data.webservice.OWMInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static fr.uavignon.cerimuseum.data.database.MuseumRoomDatabase.databaseWriteExecutor;

public class MuseumRepository {
    private static final String TAG = MuseumRepository.class.getSimpleName();

    private LiveData<List<Object>> allObjects;
    private MutableLiveData<Object> selectedObject;

    private MutableLiveData<Throwable> webServiceThrowable;

    private LiveData<List<Object>> objectsDesc;
    private LiveData<List<Object>> objectsNewest;
    private LiveData<List<Object>> objectsOldest;

    private LiveData<List<Category>> cats;

    public static Map<String, List<Object>> categories = new TreeMap<>();

    private MuseumDAO museumDAO;

    private OWMInterface api;

    private static volatile MuseumRepository INSTANCE;

    public synchronized static MuseumRepository get(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MuseumRepository(application);
        }

        return INSTANCE;
    }

    public MuseumRepository(Application application) {
        MuseumRoomDatabase db = MuseumRoomDatabase.getDatabase(application);
        museumDAO = db.museumDAO();
        allObjects = museumDAO.getAllObjects();
        objectsDesc = museumDAO.getAllObjectsDesc();
        objectsNewest = museumDAO.getAllObjectsNewest();
        objectsOldest = museumDAO.getAllObjectsOldest();

        cats = museumDAO.getAllCategories();

        selectedObject = new MutableLiveData<>();
        Retrofit retrofit=
                new Retrofit.Builder()
                        .baseUrl("https://demo-lia.univ-avignon.fr/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build();

        api = retrofit.create(OWMInterface.class);
    }

    public LiveData<List<Object>> getObjects() {
        return objectsDesc;
    }

    public LiveData<List<Object>> getObjectsNewest() {
        return objectsNewest;
    }

    public LiveData<List<Object>> getobjectsOldest() {
        return objectsOldest;
    }

    public LiveData<List<Object>> getAllObjects() {
        return allObjects;
    }

    public MutableLiveData<Object> getSelectedObject() {
        return selectedObject;
    }

    public void deleteAll(){
        museumDAO.deleteAll();
    }

    public long insertObjet(Object newObjet) {
        Future<Long> flong = databaseWriteExecutor.submit(() -> {
            return museumDAO.insert(newObjet);
        });
        long res = -1;
        try {
            res = flong.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (res != -1)
            selectedObject.setValue(newObjet);
        return res;
    }

    public long insertCategory(Category category) {
        Future<Long> flong = databaseWriteExecutor.submit(() -> {
            return museumDAO.insertCat(category);
        });
        long res = -1;
        try {
            res = flong.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (res != -1)
//            selectedObject.setValue(category);
        return res;
    }

    public int updateObject(Object object) {
        Future<Integer> fint = databaseWriteExecutor.submit(() -> {
            return museumDAO.update(object);
        });
        int res = -1;
        try {
            res = fint.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (res != -1)
            selectedObject.setValue(object);
        return res;
    }

    public void getObject(long id)  {
        Future<Object> fobject = databaseWriteExecutor.submit(() -> {
            Log.d(TAG,"selected id="+id);
            return museumDAO.getObjectById(id);
        });
        try {
            selectedObject.setValue(fobject.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Category>> getAllCategories(){
        return cats;
    }

    public void loadMuseumObjectswithKeys(){

        api.getForecast1("collection").enqueue(
                new Callback<Map<String, getCollection.ItemResponse>>() {
                    @Override
                    public void onResponse(Call<Map<String, getCollection.ItemResponse>> call, Response<Map<String, getCollection.ItemResponse>> response) {

                            for(String id : response.body().keySet()){
                                insertObjet(new Object(id));
                            }
                        Log.d(TAG,"result ="+response.body());
                    }

                    @Override
                    public void onFailure(Call<Map<String, getCollection.ItemResponse>> call, Throwable t) {
                        webServiceThrowable.postValue(t);
                    }
                }
        );
    }

    public void loadMuseumObject(Object o){
        final ObjectResult objectResult = new ObjectResult();
        api.getForecast2("items", o.getId()).enqueue(
            new Callback<ObjectResponse>() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {

                    objectResult.transferItemInfo(response.body(),o);
                    updateObject(o);
                    Log.d(TAG,"result ="+response.body());

                }
                @Override
                public void onFailure(Call<ObjectResponse> call, Throwable t) {
                    webServiceThrowable.postValue(t);
                }
            }
        );
    }

    public void loadAllObjects(List<Object> objects) {
        System.out.println("***loading all objects***");
        for (Object o : objects) {
            loadMuseumObject(o);
        }
    }

    public void loadCategories() throws IOException {
        api.getForecast3().enqueue(
                new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        for (String categoryName:response.body()) {
                            insertCategory(new Category(categoryName));
                        }
                        //setCats(response.body());
                        Log.d(TAG,"result ="+response.body());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        //webServiceThrowable.postValue(t);
                        System.out.println("failure");
                    }
                }
        );
    }
}

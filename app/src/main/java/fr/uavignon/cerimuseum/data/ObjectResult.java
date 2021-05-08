package fr.uavignon.cerimuseum.data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ObjectResult {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void transferItemInfo(ObjectResponse objectResponse, Object object) {

        object.setBrand(objectResponse.brand);

        object.setCategory(String.join(", ", objectResponse.categories));

        if (objectResponse.description != null) {
            object.setDescrption(String.join(", ", objectResponse.description));
        } else {
            object.setDescrption("Description not provided");
        }

        object.setName(objectResponse.name);

        if (objectResponse.technicalDetails != null) {
            object.setTechnicalDetails(String.join(", ", objectResponse.technicalDetails));
        } else {
            object.setTechnicalDetails("Technical details not provided");
        }

        object.setTimeFrame(String.join(", ", objectResponse.timeFrame.toString()));

        if (objectResponse.year != null) {
            object.setYear(objectResponse.year);
        }


        if (objectResponse.working != null) {
            object.setWorking("Still Working");
        } else {
            object.setWorking("Not Working");
        }

        if (objectResponse.pictures != null) {
            List<String> keys = new ArrayList<>();
            keys.addAll(objectResponse.pictures.keySet());
            object.setPictures(String.join(",", keys));
        }

    }
}

package fr.uavignon.cerimuseum.data.webservice;

import java.util.List;
import java.util.Map;

import fr.uavignon.cerimuseum.data.ObjectResponse;
import fr.uavignon.cerimuseum.data.getCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface OWMInterface {

    @Headers("Accept: application/json")

    @GET("cerimuseum/{p}")
    Call<Map<String, getCollection.ItemResponse>> getForecast1(@Path("p") String p);

    @GET("cerimuseum/{p}/{i}")
    Call<ObjectResponse> getForecast2(@Path("p") String p, @Path("i") String i);

    @GET("cerimuseum/categories")
    Call<List<String>> getForecast3();


}

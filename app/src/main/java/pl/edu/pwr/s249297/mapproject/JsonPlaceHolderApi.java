package pl.edu.pwr.s249297.mapproject;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface JsonPlaceHolderApi {

    @GET("rovers")
    Call<APIResponse> getAPIResponse();

}

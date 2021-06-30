package pl.edu.pwr.s249297.mapproject;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {
    private final FragmentManager mapFragmentManager;
    private List<Rover> rovers = new ArrayList<>();
    private final Retrofit retrofit;
    private Context context;

    public RestAdapter(FragmentManager fragmentManager, Context context){
        mapFragmentManager = fragmentManager;
        this.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(getPrefIP())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private String getPrefIP()
    {
        SharedPreferences sp = context.getSharedPreferences("pref",0);
        String str = sp.getString("server_ip","http://192.168.1.41:8888/");
        return str;
    }

    public void callApi(){

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<APIResponse> call = jsonPlaceHolderApi.getAPIResponse();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                StringBuilder content = new StringBuilder();

                if (!response.isSuccessful()){
                    content.append("Server response code: ").append(response.code());
                    return;
                }

                APIResponse responseBody = response.body();
                content.append("Status: ").append(responseBody.getStatus()).append("\n\n");

                rovers = sortRovers(responseBody.getData());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                MessageDialogFragment dialog = new MessageDialogFragment("API Communication Failure: \n" + t.getMessage());
                dialog.show(mapFragmentManager, "responseDialog");
            }
        });
    }

    public List<Rover> getRovers() {
        return rovers;
    }

    private List<Rover> sortRovers(List<Rover> rovers){
        Collections.sort(rovers, new RoversIdComp());
        return rovers;
    }

    private static class RoversIdComp implements Comparator<Rover>{
        @Override
        public int compare(Rover o1, Rover o2) {
            return o1.getRoverId().compareTo(o2.getRoverId());
        }
    }
}
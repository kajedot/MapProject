package pl.edu.pwr.s249297.mapproject;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {
    FragmentManager mapFragmentManager;
    List<Rover> rovers;

    public RestAdapter(FragmentManager fragmentManager){
        mapFragmentManager = fragmentManager;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.41:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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

                rovers = responseBody.getData();
                for (Rover rover : rovers) {
                    content.append("ID: ").append(rover.getRoverId()).append("\n");
                    content.append("cord X: ").append(rover.getCordX()).append("\n");
                    content.append("cord Y: ").append(rover.getCordY()).append("\n");
                    content.append("angle: ").append(rover.getAngle()).append("\n\n");
                }

                MessageDialogFragment dialog = new MessageDialogFragment("API Response: \n" + content);
                dialog.show(mapFragmentManager, "responseDialog");
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
}
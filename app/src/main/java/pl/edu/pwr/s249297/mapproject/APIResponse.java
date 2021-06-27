package pl.edu.pwr.s249297.mapproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class APIResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Rover> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Rover> getData() {
        return data;
    }

    public void setData(List<Rover> data) {
        this.data = data;
    }

}
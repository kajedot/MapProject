package pl.edu.pwr.s249297.mapproject;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rover {

    @SerializedName("rover_id")
    @Expose
    private Integer roverId;
    @SerializedName("cord_x")
    @Expose
    private Double cordX;
    @SerializedName("cord_y")
    @Expose
    private Double cordY;
    @SerializedName("angle")
    @Expose
    private Double angle;

    private Marker marker;

    public Integer getRoverId() {
        return roverId;
    }

    public void setRoverId(Integer roverId) {
        this.roverId = roverId;
    }

    public Double getCordX() {
        return cordX;
    }

    public void setCordX(Double cordX) {
        this.cordX = cordX;
    }

    public Double getCordY() {
        return cordY;
    }

    public void setCordY(Double cordY) {
        this.cordY = cordY;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

}
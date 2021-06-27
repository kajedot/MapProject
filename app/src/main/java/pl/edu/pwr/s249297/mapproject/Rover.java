package pl.edu.pwr.s249297.mapproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rover {

    @SerializedName("rover_id")
    @Expose
    private String roverId;
    @SerializedName("cord_x")
    @Expose
    private Double cordX;
    @SerializedName("cord_y")
    @Expose
    private Double cordY;
    @SerializedName("angle")
    @Expose
    private Double angle;

    public String getRoverId() {
        return roverId;
    }

    public void setRoverId(String roverId) {
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

}
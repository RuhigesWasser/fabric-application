package com.example.application.entity;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author ASUS
 */
public class Production {
    private String temperature;

    private String airHumidity;

    private String soilHumidity;

    private String wind;

    private String sunlight;

    private String disaster;

    private String isInsured;

    private String isCompensated;

    public Production(){
        this.setTemperature("");
        this.setAirHumidity("");
        this.setSoilHumidity("");
        this.setWind("");
        this.setSunlight("");
        this.setDisaster("");
        this.setIsInsured("");
        this.setIsCompensated("");
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(String airHumidity) {
        this.airHumidity = airHumidity;
    }

    public String getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(String soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getDisaster() {
        return disaster;
    }

    public void setDisaster(String disaster) {
        this.disaster = disaster;
    }

    public String getIsInsured() {
        return isInsured;
    }

    public void setIsInsured(String isInsured) {
        this.isInsured = isInsured;
    }

    public String getIsCompensated() {
        return isCompensated;
    }

    public void setIsCompensated(String isCompensated) {
        this.isCompensated = isCompensated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, airHumidity, soilHumidity, wind, sunlight, disaster, isInsured, isCompensated);
    }

    @Override
    public String toString() {
        return "Production{" +
                "temperature='" + temperature + '\'' +
                ", airHumidity='" + airHumidity + '\'' +
                ", soilHumidity='" + soilHumidity + '\'' +
                ", wind='" + wind + '\'' +
                ", sunlight='" + sunlight + '\'' +
                ", disaster='" + disaster + '\'' +
                ", isInsured='" + isInsured + '\'' +
                ", isCompensated='" + isCompensated + '\'' +
                '}';
    }

    public Production fromJSONString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Production.class);
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

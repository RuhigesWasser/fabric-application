package com.example.application.entity;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author ASUS
 */
public class Courier {

    private String courierId;

    private String carrierName;

    private String carrierCompany;

    private String carrierPhone;

    private String startTime;

    private String endTime;

    private String position;

    public Courier() {
        this.setCourierId("");
        this.setCarrierName("");
        this.setCarrierCompany("");
        this.setCarrierPhone("");
        this.setStartTime("");
        this.setEndTime("");
        this.setPosition("");
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierCompany() {
        return carrierCompany;
    }

    public void setCarrierCompany(String carrierCompany) {
        this.carrierCompany = carrierCompany;
    }

    public String getCarrierPhone() {
        return carrierPhone;
    }

    public void setCarrierPhone(String carrierPhone) {
        this.carrierPhone = carrierPhone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    @Override
    public int hashCode() {
        return Objects.hash(courierId, carrierName, carrierCompany, carrierPhone, startTime, endTime, position);
    }

    @Override
    public String toString() {
        return "Courier{" +
                "courierId='" + courierId + '\'' +
                ", carrierName='" + carrierName + '\'' +
                ", carrierCompany='" + carrierCompany + '\'' +
                ", carrierPhone='" + carrierPhone + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Courier fromJSONString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Courier.class);
    }
}

package com.example.application.entity;

import com.google.gson.Gson;

import java.util.Objects;

public class Org {
    private String name;

    private String id;

    private String password;

    private String type;

    private String phone;

    private String traceability;

    private String authentication;

    public Org() {
        this.name = "";
        this.id = "";
        this.password = "";
        this.type = "";
        this.phone = "";
        this.traceability = "";
        this.authentication = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTraceability() {
        return traceability;
    }

    public void setTraceability(String traceability) {
        this.traceability = traceability;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, password, type, phone, traceability, authentication);
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", phone='" + phone + '\'' +
                ", traceability='" + traceability + '\'' +
                ", authentication='" + authentication + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Org fromJSONString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Org.class);
    }
}

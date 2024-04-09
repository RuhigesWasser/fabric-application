package com.example.application.entity;

import com.google.gson.Gson;

import java.util.Objects;

public class Member {
    private String id;

    private String name;

    private String phone;

    private String email;

    private String address;

    public Member() {
        this.setId("");
        this.setName("");
        this.setPhone("");
        this.setEmail("");
        this.setAddress("");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, email, address);
    }

    @Override
    public String toString() {
        return "Dealer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Member fromJSONString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Member.class);
    }
}

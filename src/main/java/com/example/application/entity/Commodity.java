package com.example.application.entity;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author ASUS
 */
public class Commodity {

    //   商品识别信息，包含名称、条码、类别等登记于商品信息服务平台的信息
    private String barCode;

    private String name;

    private String category;

    private String brand;

    //    本批次商品生产者、品牌商、承运商、经销商、商店

    private Member brands;

    private Member producer;

    private Member carrier;

    private Member dealer;

    private Member shop;

    //    产品属性，包含溯源标识、数量（重量）、生产日期、保质期等

    private Production production;

    private String traceability;

    private String amount;

    private String MFD;

    private String EXP;

    private Courier courierDetail;

    private String launchTime;

    public Commodity() {
        this.setBrands(new Member());
        this.setProducer(new Member());
        this.setCarrier(new Member());
        this.setDealer(new Member());
        this.setShop(new Member());
        this.setProduction(new Production());
        this.setCourierDetail(new Courier());
        this.setBarCode("");
        this.setName("");
        this.setCategory("");
        this.setBrand("");
        this.setTraceability("");
        this.setAmount("");
        this.setMFD("");
        this.setEXP("");
        this.setLaunchTime("");
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Member getBrands() {
        return brands;
    }

    public void setBrands(Member brands) {
        this.brands = brands;
    }

    public Member getProducer() {
        return producer;
    }

    public void setProducer(Member producer) {
        this.producer = producer;
    }

    public Member getCarrier() {
        return carrier;
    }

    public void setCarrier(Member carrier) {
        this.carrier = carrier;
    }

    public Member getDealer() {
        return dealer;
    }

    public void setDealer(Member dealer) {
        this.dealer = dealer;
    }

    public Member getShop() {
        return shop;
    }

    public void setShop(Member shop) {
        this.shop = shop;
    }

    public String getTraceability() {
        return traceability;
    }

    public void setTraceability(String traceability) {
        this.traceability = traceability;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMFD() {
        return MFD;
    }

    public void setMFD(String MFD) {
        this.MFD = MFD;
    }

    public String getEXP() {
        return EXP;
    }

    public void setEXP(String EXP) {
        this.EXP = EXP;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public Courier getCourierDetail() {
        return courierDetail;
    }

    public void setCourierDetail(Courier courierDetail) {
        this.courierDetail = courierDetail;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }


    @Override
    public int hashCode() {
        return Objects.hash(barCode, name, category, brand, brands, producer, carrier, dealer, shop, production, traceability, amount, MFD, EXP, courierDetail, launchTime);
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "barCode='" + barCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", brands=" + brands +
                ", producer=" + producer +
                ", carrier=" + carrier +
                ", dealer=" + dealer +
                ", shop=" + shop +
                ", production=" + production +
                ", traceability='" + traceability + '\'' +
                ", amount='" + amount + '\'' +
                ", MFD='" + MFD + '\'' +
                ", EXP='" + EXP + '\'' +
                ", courierDetail=" + courierDetail +
                ", launchTime='" + launchTime + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Commodity fromJSONString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Commodity.class);
    }

}

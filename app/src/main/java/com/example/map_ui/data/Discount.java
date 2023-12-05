package com.example.map_ui.data;

import android.location.Location;

public class Discount {
    int id;
    int latitude;
   int longitude;
    String promotion_title;
    int percent_amount;
    String qr_code;
    String address;
    String promo_code;
    String discount_description;
 Float results;


    //    String name;
//    String code;
//    int discount;
//    String ccode;
    public Discount(){

    }
// data get and set  details of Discount/promosion page.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public  String getAddress(){
        return address;
    }
    public  void setAddress(String address){
        this.address = address;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getPromotion_title() {
        return promotion_title;
    }

    public void setPromotion_title(String promotion_title) {
        this.promotion_title = promotion_title;
    }

    public int getPercent_amount() {
        return percent_amount;
    }

    public void setPercent_amount(int percent_amount) {
        this.percent_amount = percent_amount;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;

    }

    public String getDiscount_description() {
        return discount_description;
    }

    public void setDiscount_description(String discount_description) {
        this.discount_description = discount_description;
    }

    public float getResults() {
        float[] results = new float[1];
        Location.distanceBetween(0,0,getLatitude (), getLongitude ()
                , results);

        return results[(int) 0.001];
    }

    public void setResults(Float results) {


        this.results = results;
    }

}

package com.example.map_ui.data;

public class GiftCard {
 int   id;
//         String name;
//        int points;
String card_title;
    int point_required;
    public GiftCard(){

    }
    // data get and set  details of Gift/Points .

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public int getPoint_required() {
        return point_required;
    }

    public void setPoint_required(int point_required) {
        this.point_required = point_required;
    }
    //
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getPoints() {
//        return points;
//    }
//
//    public void setPoints(int points) {
//        this.points = points;
//    }
}

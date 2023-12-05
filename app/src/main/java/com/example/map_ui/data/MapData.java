package com.example.map_ui.data;

import java.util.ArrayList;

public class MapData {
    ArrayList<Routes> routes = new ArrayList<Routes>();

    public ArrayList<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Routes> routes) {
        this.routes = routes;
    }
    public ArrayList<Steps>getSteps(){
        return routes.get(0).getLegs().get(0).getSteps();
    }

 class Routes{
    ArrayList<Legs> legs =new ArrayList<Legs>();

    public ArrayList<Legs> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Legs> legs) {
        this.legs = legs;
    }
}
    // data get and set  details of map routes page.
public class Legs {
    Distance distance = new Distance();
    Duration duration = new Duration();
    String end_address = "";
    String start_address = "";
    Location end_location = new Location();
    Location start_location =new Location();
    ArrayList<Steps> steps = new ArrayList<Steps>();

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Location end_location) {
        this.end_location = end_location;
    }

    public Location getStart_location() {
        return start_location;
    }

    public void setStart_location(Location start_location) {
        this.start_location = start_location;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }
}
    public class Steps {
    Distance distance =new Distance();
    Duration duration =new Duration();
    String end_address = "";
    String start_address = "";
    Location end_location =new Location();
    Location start_location =new Location();
    PolyLine polyline =new PolyLine();
    String travel_mode = "";
    String html_instructions = "";

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Location end_location) {
        this.end_location = end_location;
    }

    public Location getStart_location() {
        return start_location;
    }

    public void setStart_location(Location start_location) {
        this.start_location = start_location;
    }

    public PolyLine getPolyline() {
        return polyline;
    }

    public void setPolyline(PolyLine polyline) {
        this.polyline = polyline;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getManeuver() {
        return html_instructions;
    }

    public void setManeuver(String maneuver) {
        this.html_instructions = maneuver;
    }
}

    public class Duration {
    String text = "";
    int value = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
// Lat and log Distance Show in Promosion page.
    public class Distance {
    String text = "";
    int value = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

    public class PolyLine {
    String points = "";

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

    public class Location{
    String lat ="";
    String  lng ="";

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}}
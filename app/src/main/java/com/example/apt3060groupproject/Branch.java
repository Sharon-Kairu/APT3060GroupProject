package com.example.apt3060groupproject;

public class Branch {
    String name;
    String location;

    public Branch(String name, String location){
        this.name=name;
        this.location=location;
    }

    public String getName(){return name;}
    public String getLocation(){return location;}
}

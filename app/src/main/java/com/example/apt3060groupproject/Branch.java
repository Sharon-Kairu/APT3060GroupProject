package com.example.apt3060groupproject;

public class Branch {
    String id;
    String name;
    String location;

    public Branch( String id,String name, String location){
        this.id=id;
        this.name=name;
        this.location=location;
    }

    public Branch() {

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName(){return name;}
    public void setName(String name) { this.name=name; }

    public String getLocation(){return location;}
}

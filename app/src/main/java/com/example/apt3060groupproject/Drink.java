package com.example.apt3060groupproject;

import java.util.HashMap;
import java.util.Map;

public class Drink {
    String drink_name,brand;
    int imageResId;
    Map<String, Double> sizePriceMap;

    public Drink(String drink_name,int imageResId, String brand){
        this.drink_name=drink_name;
        this.imageResId = imageResId;
        this.brand=brand;
        this.sizePriceMap = new HashMap<>();
    }
    public String getDrinkName(){return drink_name;}
    public  int getDrinkImage() {
        return imageResId;
    }
    public String getBrand(){return brand;}

    public void addSizePrice(String size, double price) {
        sizePriceMap.put(size, price);
    }
    public double getPriceForSize(String size) {
        return sizePriceMap.getOrDefault(size, 0.0);
    }

    public Map<String, Double> getSizePriceMap() {
        return sizePriceMap;
    }

}

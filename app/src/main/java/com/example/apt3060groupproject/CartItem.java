package com.example.apt3060groupproject;

public class CartItem {
    String item_name;
     int image;
     String size;

     int quantity;
     double price;

     public CartItem(String item_name,int image,String size,int quantity,double price){
         this.item_name=item_name;
         this.image=image;
         this.size=size;
         this.quantity=1;
         this.price=price;
     }

}

package com.example.apt3060groupproject;

public class CartItem {
    String id;
    private String userId;
    String item_name;
     int image;
     String size;
     int quantity;
     double price;
    private String branchId;

    public CartItem() {}

     public CartItem(String item_name,int image,String size,int quantity,double price){
         this.item_name=item_name;
         this.image=image;
         this.size=size;
         this.quantity=quantity;
         this.price=price;
     }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }


    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getItem_name(){return item_name;}
    public int getImage(){return image;}


    public String getSize(){return size;}
    public int getQuantity(){return quantity;}

    public double getPrice(){return price;}

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}


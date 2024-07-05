package com.example.apt3060groupproject;

public class User {
    String names,email,phone_number,password;

    public User(String names,String email, String phone_number,String password){
        this.names=names;
        this.email=email;
        this.phone_number=phone_number;
        this.password=password;
    }

    public String getNames(){return names;}

    public String getEmail(){return email;}

    public String getPhone_number(){return phone_number;}

    public String getPassword(){return password;}
}

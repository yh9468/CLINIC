package com.example.younho.clinic.model;

import java.util.ArrayList;

public class MyUser {
    private String ID;
    private String PW;
    private String Phone;
    private boolean IsMale;
    private String BirthDate;
    private String eMail;
    private ArrayList<Integer> FavoriteLaundry;
    private ArrayList<Integer> FavoriteRepair;

    public MyUser(){}

    public MyUser(String ID, String PW, String phone, boolean isMale, String birthDate, String eMail, ArrayList<Integer> favoriteLaundry, ArrayList<Integer> favoriteRepair) {
        this.ID = ID;
        this.PW = PW;
        Phone = phone;
        IsMale = isMale;
        BirthDate = birthDate;
        this.eMail = eMail;
        FavoriteLaundry = favoriteLaundry;
        FavoriteRepair = favoriteRepair;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean isMale() {
        return IsMale;
    }

    public void setMale(boolean male) {
        IsMale = male;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public ArrayList<Integer> getFavoriteLaundry() {
        return FavoriteLaundry;
    }

    public void setFavoriteLaundry(ArrayList<Integer> favoriteLaundry) {
        FavoriteLaundry = favoriteLaundry;
    }

    public ArrayList<Integer> getFavoriteRepair() {
        return FavoriteRepair;
    }

    public void setFavoriteRepair(ArrayList<Integer> favoriteRepair) {
        FavoriteRepair = favoriteRepair;
    }
}
